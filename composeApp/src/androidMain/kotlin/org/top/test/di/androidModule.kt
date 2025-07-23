package org.top.test.di

import android.content.Context
import com.arkivanov.decompose.ComponentContext
import com.top.messengerdynamicfeature.Messenger
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import org.top.test.dynamicFeatures.dynamicfeature.DefaultFeatureInstaller
import org.top.test.dynamicFeatures.dynamicfeature.DynamicFeatureComponent
import org.top.test.dynamicFeatures.dynamicfeature.FeatureInstaller
import org.top.test.dynamicFeatures.feature
import org.top.test.dynamicFeatures.messenger.Messenger

fun getAndroidModule(componentContext: ComponentContext, context: Context) = module {
    single<Context> { context }
    single<ComponentContext> { componentContext }
    single<FeatureInstaller> {
        DefaultFeatureInstaller(context = get())
    }
    single<DynamicFeatureComponent<Messenger>>{
        feature(get())
    }
    single<(ComponentContext) -> Messenger>(qualifier = qualifier(MyQualifiers.Messenger.value)) {
        {
            Messenger(get())
        }
    }
}