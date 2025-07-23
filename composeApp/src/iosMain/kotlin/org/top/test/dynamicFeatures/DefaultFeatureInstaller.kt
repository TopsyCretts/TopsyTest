package org.top.test.dynamicFeatures

import org.top.test.dynamicFeatures.dynamicfeature.FeatureInstaller


object DefaultFeatureInstaller : FeatureInstaller {

    override suspend fun install(name: String): FeatureInstaller.Result {
        return FeatureInstaller.Result.Installed
    }
}
