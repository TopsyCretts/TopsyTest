package org.top.test

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.compose.koinInject
import org.koin.core.context.startKoin
import org.koin.core.error.KoinApplicationAlreadyStartedException
import org.koin.core.module.Module
import org.top.test.root.RootComponent
import org.top.test.root.RootContent

@Composable
fun MainApplication(
    platformModules: List<Module> = emptyList()
) {
    try {
        startKoin {
            modules(
                platformModules
            )
        }
    } catch (_: KoinApplicationAlreadyStartedException) {
    }
    RootContent(RootComponent.factory(koinInject()),
        Modifier
            .fillMaxSize()
    )
}