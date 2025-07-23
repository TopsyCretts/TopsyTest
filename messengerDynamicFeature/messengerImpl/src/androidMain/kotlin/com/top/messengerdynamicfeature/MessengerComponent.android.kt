package com.top.messengerdynamicfeature

import com.arkivanov.decompose.value.Value

actual fun getMessengerClient(): MessengerClient {
    return AndroidMessengerClient()
}


class AndroidMessengerClient() : MessengerClient {

    val tgClient = TelegramClient

    override val authState: Value<MessengerClient.AuthenticationState>
        get() = tgClient.authState

    override suspend fun insertPhoneNumber(number: String) {
        tgClient.insertPhoneNumber(number)
    }

    override suspend fun insertCode(code: String) {
        tgClient.insertCode(code)
    }

    override suspend fun insertPassword(password: String) {
        tgClient.insertPassword(password)
    }

    override fun close() {
        tgClient.close()
    }

}