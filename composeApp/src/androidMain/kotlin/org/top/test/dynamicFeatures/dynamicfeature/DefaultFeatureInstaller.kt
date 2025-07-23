package org.top.test.dynamicFeatures.dynamicfeature

import android.content.Context
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallSessionState
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import org.top.test.dynamicFeatures.dynamicfeature.FeatureInstaller.Result
import kotlin.coroutines.resume
import kotlin.time.Duration.Companion.seconds

class DefaultFeatureInstaller(
    context: Context
) : FeatureInstaller {

    private val manager = SplitInstallManagerFactory.create(context)
    private val installedFeatures = HashSet<String>()

    override suspend fun install(name: String): Result {
        return if (isFeatureInstalled(name)) {
            Result.Installed
        } else {
            try {
                val result = installFeature(name)
                if (result == Result.Installed && name !in installedFeatures) {
                    delay(3.seconds)
                    installedFeatures += name
                }
                result
            } catch (e: Exception) {
                Result.Error
            }
        }
    }

    private fun isFeatureInstalled(name: String): Boolean {
        return name in manager.installedModules
    }

    private suspend fun installFeature(name: String): Result =
        suspendCancellableCoroutine { continuation ->
            val listener = object : SplitInstallStateUpdatedListener {
                var sessionId: Int = 0

                override fun onStateUpdate(state: SplitInstallSessionState) {
                    if (state.sessionId() != sessionId) return

                    when (state.status()) {
                        SplitInstallSessionStatus.INSTALLED -> {
                            manager.unregisterListener(this)
                            continuation.resume(Result.Installed)
                        }

                        SplitInstallSessionStatus.CANCELED -> {
                            manager.unregisterListener(this)
                            continuation.resume(Result.Cancelled)
                        }

                        SplitInstallSessionStatus.FAILED -> {
                            manager.unregisterListener(this)
                            continuation.resume(Result.Error)
                        }

                        else -> Unit // You can handle PROGRESS, etc., if needed
                    }
                }
            }

            manager.registerListener(listener)

            val request = SplitInstallRequest.newBuilder()
                .addModule(name)
                .build()

            manager
                .startInstall(request)
                .addOnSuccessListener { id ->
                    listener.sessionId = id
                }
                .addOnFailureListener { ex ->
                    manager.unregisterListener(listener)
                    continuation.resume(Result.Error)
                }

            continuation.invokeOnCancellation {
                manager.unregisterListener(listener)
                if (listener.sessionId != 0) {
                    manager.cancelInstall(listener.sessionId)
                }
            }
        }
}
