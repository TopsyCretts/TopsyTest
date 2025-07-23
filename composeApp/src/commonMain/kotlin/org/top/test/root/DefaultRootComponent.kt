package org.top.test.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import org.top.test.Url
import org.top.test.tabs.TabsComponent

class DefaultRootComponent(
    componentContext: ComponentContext,
) : RootComponent, ComponentContext by componentContext {

    private val nav = StackNavigation<Config>()

    private val _stack =
        childStack(
            source = nav,
            serializer = Config.serializer(),
            initialStack = { getInitialStack() },
            childFactory = ::child,
        )

    override val stack: Value<ChildStack<*, RootComponent.Child>> = _stack


    private fun child(config: Config, componentContext: ComponentContext): RootComponent.Child =
        when (config) {
            is Config.Tabs ->
                RootComponent.Child.TabsChild(
                    TabsComponent.factory(componentContext)
                )
        }

    override fun onBackClicked() {
        nav.pop()
    }

    private fun getInitialStack(): List<Config> {
        return listOf(Config.Tabs())
    }
}

@Serializable
private sealed interface Config {
    @Serializable
    data class Tabs(val deepLinkUrl: Url? = null) : Config
}

