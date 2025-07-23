package org.top.test.tabs

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import org.top.test.di.MyQualifiers
import org.top.test.dynamicFeatures.dynamicfeature.DynamicFeatureComponent
import org.top.test.helloCMP.HelloCMPComponent
import org.top.test.tabs.TabsComponent.Child

internal class DefaultTabsComponent(
    componentContext: ComponentContext,
) : TabsComponent, ComponentContext by componentContext {

    private val nav = StackNavigation<Config>()

    private val _stack: Value<ChildStack<Config, Child>> =
        childStack(
            source = nav,
            serializer = Config.serializer(),
            initialConfiguration = Config.HelloCMP,
            childFactory = ::child,
        )

    override val stack: Value<ChildStack<*, Child>> = _stack
    override fun onHelloCMPTabClicked() {
        nav.bringToFront(Config.HelloCMP)
    }

    override fun onMessengerTabClicked() {
        nav.bringToFront(Config.Messenger)
    }

    private fun child(config: Config, componentContext: ComponentContext): Child =
        when (config) {
            is Config.HelloCMP ->
                Child.HelloCMPChild(
                    HelloCMPComponent.factory(componentContext)
                )

            is Config.Messenger -> Child.MessengerChild(
                DynamicFeatureComponent.factory(
                    componentContext = componentContext,
                    name = MyQualifiers.Messenger.value
                )
            )
        }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object HelloCMP : Config

        @Serializable
        data object Messenger : Config
    }
}
