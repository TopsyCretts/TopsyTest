package org.top.test.dynamicFeatures.messenger

import com.arkivanov.decompose.ComponentContext
import com.top.messengerdynamicfeature.Messenger
import com.top.messengerdynamicfeature.MessengerComponent

internal fun Messenger(
    componentContext: ComponentContext,
): Messenger {
   return MessengerComponent(componentContext)
}