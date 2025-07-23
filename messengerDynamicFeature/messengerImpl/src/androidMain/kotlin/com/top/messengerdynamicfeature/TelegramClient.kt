package com.top.messengerdynamicfeature

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import org.drinkless.tdlib.Client
import org.drinkless.tdlib.TdApi
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import com.top.messengerdynamicfeature.MessengerClient.AuthenticationState.*
import com.top.messengerdynamicfeature.messengerImpl.BuildConfig
import kotlinx.coroutines.channels.onFailure
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Locale

@SuppressLint("StaticFieldLeak")
object TelegramClient : Client.ResultHandler, KoinComponent{

    private const val TAG = "TelegramClient"

    var client: Client? = Client.create(this, null, null)
    val context: Context by inject<Context>()

    var hadInitialized = false

    private val _authState = MutableValue<MessengerClient.AuthenticationState>(UNKNOWN)
    val authState: Value<MessengerClient.AuthenticationState> get() = _authState

    init {
        client?.send(TdApi.SetLogVerbosityLevel(1), this)
        client?.send(TdApi.GetAuthorizationState(), this)
    }

    private val requestScope = CoroutineScope(Dispatchers.IO)

    private fun setAuth(auth: MessengerClient.AuthenticationState) {
        _authState.value = auth
    }

    override fun onResult(data: TdApi.Object) {
        Log.d(TAG, "onResult: ${data::class.java.simpleName}")
        when (data.constructor) {
            TdApi.UpdateAuthorizationState.CONSTRUCTOR -> {
                Log.d(TAG, "UpdateAuthorizationState")
                onAuthorizationStateUpdated((data as TdApi.UpdateAuthorizationState).authorizationState)
            }

            TdApi.UpdateOption.CONSTRUCTOR -> {

            }

            else -> Log.d(TAG, "Unhandled onResult call with data: $data.")
        }
    }

    private fun doAsync(job: () -> Unit) {
        requestScope.launch { job() }
    }

    fun insertPhoneNumber(phoneNumber: String) {
        Log.d("TelegramClient", "phoneNumber: $phoneNumber")
        val settings = TdApi.PhoneNumberAuthenticationSettings().apply {
            allowFlashCall = false
            allowMissedCall = false
            allowSmsRetrieverApi = false
        }
        client?.send(TdApi.SetAuthenticationPhoneNumber(phoneNumber, settings)) {
            Log.d("TelegramClient", "phoneNumber. result: $it")
            when (it.constructor) {
                TdApi.Ok.CONSTRUCTOR -> {

                }

                TdApi.Error.CONSTRUCTOR -> {

                }
            }
        }
    }

    fun close(){
        client?.send(TdApi.Close()){
            Log.d("TelegramClient", "close. result: $it")
        }
    }

    fun insertCode(code: String) {
        Log.d("TelegramClient", "code: $code")
        doAsync {
            client?.send(TdApi.CheckAuthenticationCode(code)) {
                when (it.constructor) {
                    TdApi.Ok.CONSTRUCTOR -> {

                    }

                    TdApi.Error.CONSTRUCTOR -> {

                    }
                }
            }
        }
    }

    fun insertPassword(password: String) {
        Log.d("TelegramClient", "inserting password")
        doAsync {
            client?.send(TdApi.CheckAuthenticationPassword(password)) {
                when (it.constructor) {
                    TdApi.Ok.CONSTRUCTOR -> {

                    }

                    TdApi.Error.CONSTRUCTOR -> {

                    }
                }
            }
        }
    }

    fun setTDLibParameters() {
        client?.send(TdApi.SetTdlibParameters().apply {
            apiId = BuildConfig.TDLIB_API_ID.toInt()
            apiHash = BuildConfig.TDLIB_API_HASH
            useMessageDatabase = true
            useSecretChats = true
            systemLanguageCode = Locale.getDefault().language
            deviceModel = "Android"
            applicationVersion = "1.0"
            databaseDirectory = context.filesDir.absolutePath + "/td"
            filesDirectory = context.filesDir.absolutePath + "/td"
        }) {
            Log.d(TAG, "SetTdlibParameters result: $it")
            when (it.constructor) {
                TdApi.Ok.CONSTRUCTOR -> {
                    Log.d(TAG, "startAuthentication OK")
                    hadInitialized = true
                }

                TdApi.Error.CONSTRUCTOR -> {
                    Log.d(TAG, "startAuthentication ERROR")
                }
            }
        }
    }

    private fun onAuthorizationStateUpdated(authorizationState: TdApi.AuthorizationState) {
        when (authorizationState.constructor) {
            TdApi.AuthorizationStateWaitTdlibParameters.CONSTRUCTOR -> {
                Log.d(
                    TAG,
                    "onResult: AuthorizationStateWaitTdlibParameters -> state = UNAUTHENTICATED"
                )
                setTDLibParameters()
            }

            TdApi.AuthorizationStateWaitPhoneNumber.CONSTRUCTOR -> {
                Log.d(TAG, "onResult: AuthorizationStateWaitPhoneNumber -> state = WAIT_FOR_NUMBER")
                setAuth(WAIT_FOR_NUMBER)
            }

            TdApi.AuthorizationStateWaitCode.CONSTRUCTOR -> {
                Log.d(TAG, "onResult: AuthorizationStateWaitCode -> state = WAIT_FOR_CODE")
                setAuth(WAIT_FOR_CODE)
            }

            TdApi.AuthorizationStateWaitPassword.CONSTRUCTOR -> {
                Log.d(TAG, "onResult: AuthorizationStateWaitPassword")
                setAuth(WAIT_FOR_PASSWORD)
            }

            TdApi.AuthorizationStateReady.CONSTRUCTOR -> {
                Log.d(TAG, "onResult: AuthorizationStateReady -> state = AUTHENTICATED")
                setAuth(AUTHENTICATED)
            }

            TdApi.AuthorizationStateLoggingOut.CONSTRUCTOR -> {
                Log.d(TAG, "onResult: AuthorizationStateLoggingOut")
                setAuth(UNAUTHENTICATED)
            }

            TdApi.AuthorizationStateClosing.CONSTRUCTOR -> {
                Log.d(TAG, "onResult: AuthorizationStateClosing")
            }

            TdApi.AuthorizationStateClosed.CONSTRUCTOR -> {
                Log.d(TAG, "onResult: AuthorizationStateClosed")
            }

            else -> Log.d(TAG, "Unhandled authorizationState with data: $authorizationState.")
        }
    }

    fun downloadFile(fileId: Int): Flow<Unit> = callbackFlow {
        client?.send(TdApi.DownloadFile(fileId, 1, 0, 0, true)) {
            when (it.constructor) {
                TdApi.Ok.CONSTRUCTOR -> {
                    trySend(Unit).isSuccess
                }

                else -> {
                    cancel("", Exception(""))

                }
            }
        }
        awaitClose()
    }


    fun <T : TdApi.Object> sendAsFlowTyped(query: TdApi.Function<T>): Flow<T> = callbackFlow {
        client?.send(query) { result ->
            when (result) {
                is TdApi.Error -> {
                    close(RuntimeException("TDLib Error: ${result.code} ${result.message}"))
                }

                else -> {
                    @Suppress("UNCHECKED_CAST")
                    trySend(result as T).onFailure { close(it) }
                }
            }
        }

        awaitClose { }
    }


    inline fun <reified T : TdApi.Object> send(query: TdApi.Function<T>): Flow<T> =
        sendAsFlowTyped(query).map {
            it
        }

}
