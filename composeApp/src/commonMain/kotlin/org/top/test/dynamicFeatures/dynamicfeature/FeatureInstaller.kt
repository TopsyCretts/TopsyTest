package org.top.test.dynamicFeatures.dynamicfeature

interface FeatureInstaller {
    suspend fun install(name: String): Result

    sealed interface Result {
        object Installed : Result
        object Cancelled : Result
        object Error : Result
    }
}

