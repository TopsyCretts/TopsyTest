plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    id("com.android.library")
}

android {
    namespace = "com.top.messengerdynamicfeature.api"

    defaultConfig {
        compileSdk = libs.versions.android.compileSdk.get().toInt()
    }
}

kotlin {

    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.decompose)
                implementation(libs.composeMultiplatform.runtime)
            }
        }
    }

}