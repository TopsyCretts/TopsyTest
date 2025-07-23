package org.top.test.helloCMP

import com.arkivanov.decompose.ComponentContext

class DefaultHelloCMPComponent(
    componentContext: ComponentContext,
    platformString: String
) : HelloCMPComponent, ComponentContext by componentContext {

    override val platform: String = platformString

}