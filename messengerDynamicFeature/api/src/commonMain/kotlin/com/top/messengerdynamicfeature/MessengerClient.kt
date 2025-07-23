package com.top.messengerdynamicfeature

import com.arkivanov.decompose.value.Value

interface MessengerClient {

    val authState: Value<AuthenticationState>

    suspend fun insertPhoneNumber(number: String)

    suspend fun insertCode(code: String)

    suspend fun insertPassword(password: String)

    fun close()

    sealed class AuthenticationState {
        object UNAUTHENTICATED : AuthenticationState()
        object UNKNOWN : AuthenticationState()
        object WAIT_FOR_NUMBER : AuthenticationState()
        object WAIT_FOR_CODE : AuthenticationState()
        object WAIT_FOR_PASSWORD : AuthenticationState()
        object AUTHENTICATED : AuthenticationState()
    }
}