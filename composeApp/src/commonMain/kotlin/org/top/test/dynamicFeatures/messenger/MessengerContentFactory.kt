package org.top.test.dynamicFeatures.messenger

import com.top.messengerDynamicFeature.dynamicfeatures.DynamicFeatureContent
import com.top.messengerdynamicfeature.Messenger

internal expect fun messengerContent(): DynamicFeatureContent<Messenger>
