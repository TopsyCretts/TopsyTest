package org.top.test.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import org.koin.core.component.KoinComponent
import org.top.test.tabs.TabsComponent

interface RootComponent : BackHandlerOwner {
    val stack: Value<ChildStack<*, Child>>

    fun onBackClicked()

    sealed class Child {
        class TabsChild(val component: TabsComponent) : Child()
    }

    companion object : KoinComponent {
        fun factory(componentContext: ComponentContext): RootComponent =
            DefaultRootComponent(
                componentContext = componentContext,
            )
    }
}