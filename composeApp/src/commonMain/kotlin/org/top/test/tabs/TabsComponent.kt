package org.top.test.tabs

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.top.messengerdynamicfeature.Messenger
import org.koin.core.component.KoinComponent
import org.top.test.dynamicFeatures.dynamicfeature.DynamicFeatureComponent
import org.top.test.helloCMP.HelloCMPComponent

interface TabsComponent {

    val stack: Value<ChildStack<*, Child>>

    fun onHelloCMPTabClicked()
    fun onMessengerTabClicked()

    sealed class Child {
        class HelloCMPChild(val component: HelloCMPComponent) : Child()
        class MessengerChild(val component: DynamicFeatureComponent<Messenger>) : Child()
    }

    companion object : KoinComponent {
        fun factory(componentContext: ComponentContext): TabsComponent =
            DefaultTabsComponent(
                componentContext = componentContext,
            )
    }
}
