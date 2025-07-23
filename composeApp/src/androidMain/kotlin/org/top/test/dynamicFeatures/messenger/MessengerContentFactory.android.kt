package org.top.test.dynamicFeatures.messenger

import com.top.messengerDynamicFeature.dynamicfeatures.DynamicFeatureContent
import com.top.messengerdynamicfeature.Messenger
import org.top.test.dynamicFeatures.featureContent

internal actual fun messengerContent(): DynamicFeatureContent<Messenger> = featureContent()
