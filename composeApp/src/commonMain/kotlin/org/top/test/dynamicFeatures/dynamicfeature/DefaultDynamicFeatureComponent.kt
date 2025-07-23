package org.top.test.dynamicFeatures.dynamicfeature

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.top.test.dynamicFeatures.dynamicfeature.DynamicFeatureComponent.Child
import org.top.test.dynamicFeatures.dynamicfeature.DynamicFeatureComponent.Child.ErrorChild
import org.top.test.dynamicFeatures.dynamicfeature.DynamicFeatureComponent.Child.FeatureChild
import org.top.test.dynamicFeatures.dynamicfeature.DynamicFeatureComponent.Child.LoadingChild
import kotlinx.serialization.Serializable

internal class DefaultDynamicFeatureComponent<out T : Any>(
    componentContext: ComponentContext,
    private val name: String,
    private val featureInstaller: FeatureInstaller,
    private val factory: (ComponentContext) -> T
) : DynamicFeatureComponent<T>, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    private val stack =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.Loading,
            childFactory = ::child,
        )

    override val childStack: Value<ChildStack<*, Child<T>>> get() = stack

    private var coroutineScope: CoroutineScope? = null

    private fun child(config: Config, componentContext: ComponentContext): Child<T> =
        when (config) {
            is Config.Loading -> loading(componentContext)
            is Config.Feature -> FeatureChild(factory(componentContext))
            is Config.Error -> ErrorChild(name = name)
        }

    private fun loading(componentContext: ComponentContext): LoadingChild {
        val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
        coroutineScope = scope
        componentContext.lifecycle.doOnCreate {
            scope.launch {
                when (featureInstaller.install(name)) {
                    FeatureInstaller.Result.Installed -> navigation.replaceCurrent(Config.Feature)
                    FeatureInstaller.Result.Cancelled,
                    FeatureInstaller.Result.Error -> navigation.replaceCurrent(Config.Error)
                }
            }
        }

        componentContext.lifecycle.doOnDestroy {
            scope.cancel()
            coroutineScope = null
        }

        return LoadingChild(name = name)
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Loading : Config

        @Serializable
        data object Feature : Config

        @Serializable
        data object Error : Config
    }
}
