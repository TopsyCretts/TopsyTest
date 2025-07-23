package org.top.test.di

import com.arkivanov.decompose.ComponentContext
import com.top.messengerdynamicfeature.Messenger
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import org.top.test.dynamicFeatures.DefaultFeatureInstaller
import org.top.test.dynamicFeatures.dynamicfeature.FeatureInstaller
import org.top.test.dynamicFeatures.messenger.Messenger

fun getIosModule(componentContext: ComponentContext) = module {
    single<ComponentContext> { componentContext }
    single<FeatureInstaller> {
        DefaultFeatureInstaller
    }
    single<(ComponentContext) -> Messenger>(qualifier = qualifier(MyQualifiers.Messenger.value)) {
        {
            Messenger(get())
        }
    }
}