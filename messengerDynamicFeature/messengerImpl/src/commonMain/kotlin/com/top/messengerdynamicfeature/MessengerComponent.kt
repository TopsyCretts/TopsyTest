package com.top.messengerdynamicfeature

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.top.messengerdynamicfeature.Messenger.AuthUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class MessengerComponent(
    componentContext: ComponentContext,
) : Messenger, ComponentContext by componentContext, KoinComponent {
    override val client: MessengerClient
        get() = getMessengerClient()

    private val _uiState: MutableValue<AuthUiState> = MutableValue(AuthUiState.Loading)

    override val uiState: Value<AuthUiState> = _uiState

    val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    init {
        scope.launch {
            client.authState.subscribe {
                _uiState.value =  when (it) {
                    MessengerClient.AuthenticationState.AUTHENTICATED -> { AuthUiState.Authenticated }
                    MessengerClient.AuthenticationState.UNAUTHENTICATED -> AuthUiState.Loading
                    MessengerClient.AuthenticationState.UNKNOWN -> AuthUiState.Loading
                    MessengerClient.AuthenticationState.WAIT_FOR_CODE -> AuthUiState.InsertCode()
                    MessengerClient.AuthenticationState.WAIT_FOR_NUMBER -> AuthUiState.InsertNumber()
                    MessengerClient.AuthenticationState.WAIT_FOR_PASSWORD -> AuthUiState.InsertPassword()
                }
            }
        }

            lifecycle.doOnDestroy {
                client.close()
            }

    }

    override fun insertPhoneNumber(number: String) {
        _uiState.value = AuthUiState.Loading
        scope.launch {
            client.insertPhoneNumber(number)
        }

    }

    override fun insertCode(code: String) {
        _uiState.value = AuthUiState.Loading
        scope.launch {
            client.insertCode(code)
        }
    }

    override fun insertPassword(password: String) {
        _uiState.value = AuthUiState.Loading
        scope.launch {
            client.insertPassword(password)
        }
    }

}

expect fun getMessengerClient() : MessengerClient