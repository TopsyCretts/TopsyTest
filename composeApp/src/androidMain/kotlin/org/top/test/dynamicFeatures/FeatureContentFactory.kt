package org.top.test.dynamicFeatures

import com.top.messengerDynamicFeature.dynamicfeatures.DynamicFeatureContent


@Suppress("UNCHECKED_CAST")
internal inline fun <reified T : Any> featureContent(): DynamicFeatureContent<T> =
    Class.forName("${T::class.java.name}Content").getDeclaredConstructor().newInstance() as DynamicFeatureContent<T>
