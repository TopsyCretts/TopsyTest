package org.top.test

import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.ApplicationLifecycle
import org.top.test.di.getIosModule

fun MainViewController() = ComposeUIViewController {
    MainApplication(listOf(getIosModule(DefaultComponentContext(ApplicationLifecycle()))))
}