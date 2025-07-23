package org.top.test.dynamicFeatures.messenger

import com.arkivanov.decompose.ComponentContext
import com.top.messengerdynamicfeature.Messenger
import org.top.test.dynamicFeatures.feature

internal fun Messenger(
    componentContext: ComponentContext,
): Messenger = feature(componentContext)