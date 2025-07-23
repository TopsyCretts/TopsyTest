package org.top.test.dynamicFeatures.dynamicfeature

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.qualifier.qualifier

interface DynamicFeatureComponent<out T : Any> {

    val childStack: Value<ChildStack<*, Child<T>>>

    sealed class Child<out T : Any> {
        class LoadingChild(val name: String) : Child<Nothing>()
        class FeatureChild<out T : Any>(val feature: T) : Child<T>()
        class ErrorChild(val name: String) : Child<Nothing>()
    }

    companion object : KoinComponent {
        fun <T: Any> factory(componentContext: ComponentContext, name: String): DynamicFeatureComponent<T> =
            DefaultDynamicFeatureComponent(
                componentContext = componentContext,
                name = name,
                featureInstaller = get(),
                factory = get(qualifier = qualifier(name))
            )
    }
}
