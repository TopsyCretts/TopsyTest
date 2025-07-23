package org.top.test.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.FrontHand
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.top.test.dynamicFeatures.dynamicfeature.DynamicFeatureContent
import org.top.test.dynamicFeatures.messenger.messengerContent
import org.top.test.helloCMP.HelloCMPContent

@Composable
internal fun TabsContent(
    component: TabsComponent,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Children(
            component = component,
            modifier = Modifier.weight(1F).consumeWindowInsets(WindowInsets.navigationBars)
        )
        BottomBar(component = component, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun Children(component: TabsComponent, modifier: Modifier = Modifier) {
    Children(
        stack = component.stack,
        modifier = modifier,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is TabsComponent.Child.HelloCMPChild -> {
                HelloCMPContent(child.component)
            }

            is TabsComponent.Child.MessengerChild -> {
                DynamicFeatureContent(
                    component = child.component,
                    modifier = Modifier.fillMaxSize(),
                    contentSupplier = ::messengerContent,
                )
            }
        }
    }
}

@Composable
private fun BottomBar(component: TabsComponent, modifier: Modifier = Modifier) {
    val stack by component.stack.subscribeAsState()
    val activeComponent = stack.active.instance

    BottomAppBar(
        modifier = modifier,
        actions = {
            NavigationBarItem(
                selected = activeComponent is TabsComponent.Child.HelloCMPChild,
                onClick = {
                    component.onHelloCMPTabClicked()
                },
                icon = {
                    Icon(Icons.Filled.FrontHand, contentDescription = "Localized description")
                }
            )

            NavigationBarItem(
                selected = activeComponent is TabsComponent.Child.MessengerChild,
                onClick = {
                    component.onMessengerTabClicked()
                },
                icon = {
                    Icon(
                        Icons.AutoMirrored.Filled.Chat,
                        contentDescription = "Localized description"
                    )
                }
            )
        }
    )
}

