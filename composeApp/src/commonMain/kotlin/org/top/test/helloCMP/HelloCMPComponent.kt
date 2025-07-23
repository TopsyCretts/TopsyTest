package org.top.test.helloCMP

import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.KoinComponent
import org.top.test.Greeting

interface HelloCMPComponent {
    val platform: String

    companion object : KoinComponent {
        fun factory(componentContext: ComponentContext): HelloCMPComponent =
            DefaultHelloCMPComponent(
                componentContext = componentContext,
                platformString = Greeting().greet()
            )
    }
}