package com.top.messengerdynamicfeature

import com.arkivanov.decompose.value.Value

interface Messenger {

    val client: MessengerClient

    val uiState: Value<AuthUiState>

    fun insertPhoneNumber(number: String)

    fun insertCode(code: String)

    fun insertPassword(password: String)

    sealed class AuthUiState {
        object Loading : AuthUiState()
        data class InsertNumber(val previousError: Throwable? = null) : AuthUiState()
        data class InsertCode(val previousError: Throwable? = null) : AuthUiState()
        data class InsertPassword(val previousError: Throwable? = null) : AuthUiState()
        object Authenticated : AuthUiState()
    }

}

