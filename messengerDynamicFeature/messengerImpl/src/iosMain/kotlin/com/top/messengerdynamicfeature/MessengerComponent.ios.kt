package com.top.messengerdynamicfeature

actual fun getMessengerClient(): MessengerClient {
    return messengerClient
}

lateinit var messengerClient : MessengerClient
