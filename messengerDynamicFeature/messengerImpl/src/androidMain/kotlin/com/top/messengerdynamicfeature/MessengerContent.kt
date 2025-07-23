package com.top.messengerdynamicfeature

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.top.messengerDynamicFeature.dynamicfeatures.DynamicFeatureContent
import com.top.messengerdynamicfeature.Messenger.AuthUiState.*

@Suppress("unused") // Used via reflection
class MessengerContent : DynamicFeatureContent<Messenger> {

    @Suppress("ComposableNaming")
    @Composable
    override fun invoke(component: Messenger, modifier: Modifier) {
        val uiState by component.uiState.subscribeAsState()

        when (uiState) {
            is InsertNumber -> WaitForNumberScreen {
                component.insertPhoneNumber(it)
            }
            is InsertCode -> WaitForCodeScreen {
                component.insertCode(it)
            }
            is InsertPassword -> WaitForPasswordScreen {
                component.insertPassword(it)
            }
            Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            Authenticated -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Welcome!")
                }
            }
        }
    }
}


@Composable
fun WaitForNumberScreen(onEnter: (String) -> Unit) {
    AuthorizationScreen(
        title = "Enter phone number",
        message = "Please enter your number in international format",
        onEnter = onEnter
    )
}

@Composable
fun WaitForCodeScreen(onEnter: (String) -> Unit) {
    AuthorizationScreen(
        title = "Enter code",
        onEnter = onEnter
    )
}

@Composable
fun WaitForPasswordScreen(onEnter: (String) -> Unit) {
    AuthorizationScreen(
        title = "Enter password",
        onEnter = onEnter
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AuthorizationScreen(title: String, message: String? = null, onEnter: (String) -> Unit) {
    val executed = remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(title) })
        },
        content = {paddings ->
            if (executed.value) {
                CircularProgressIndicator()
            } else {
                val phoneNumber = remember { mutableStateOf(TextFieldValue()) }
                Column(
                    modifier = Modifier.padding(paddingValues = paddings).padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    TextField(
                        value = phoneNumber.value,
                        onValueChange = { phoneNumber.value = it },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = MaterialTheme.typography.bodyMedium
                    )
                    if (message == null) {
                        Spacer(modifier = Modifier.height(16.dp))
                    } else {
                        Text(message, modifier = Modifier.padding(vertical = 16.dp))
                    }
                    Button(onClick = {
                        onEnter(phoneNumber.value.text)
                        executed.value = true
                    }, modifier = Modifier.align(Alignment.End)) {
                        Text("Enter")
                    }
                }
            }
        }
    )
}