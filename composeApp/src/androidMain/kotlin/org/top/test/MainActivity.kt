package org.top.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.defaultComponentContext
import org.top.test.di.getAndroidModule

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            MainApplication(
                platformModules = listOf(
                    getAndroidModule(
                        componentContext = defaultComponentContext(),
                        context = this
                    )
                )
            )
        }
    }
}