package org.top.test.dynamicFeatures.dynamicfeature

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.top.messengerDynamicFeature.dynamicfeatures.DynamicFeatureContent

@Composable
fun <T : Any> DynamicFeatureContent(
    component: DynamicFeatureComponent<T>,
    modifier: Modifier = Modifier,
    contentSupplier: () -> DynamicFeatureContent<T>,
) {
    Children(
        stack = component.childStack,
        modifier = modifier,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is DynamicFeatureComponent.Child.LoadingChild -> Loading(name = child.name, modifier = Modifier.fillMaxSize())

            is DynamicFeatureComponent.Child.FeatureChild -> {
                val content = remember(contentSupplier)
                content(component = child.feature, modifier = Modifier.fillMaxSize())
            }

            is DynamicFeatureComponent.Child.ErrorChild -> Error(name = child.name, modifier = Modifier.fillMaxSize())
        }.let {}
    }
}

@Composable
private fun Loading(name: String, modifier: Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text(text = "Loading $name...")
    }
}

@Composable
private fun Error(name: String, modifier: Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text(text = "Error loading $name...")
    }
}
