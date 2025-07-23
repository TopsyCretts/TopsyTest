
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeCompiler)
}

kotlin {

    android {
        namespace = "com.top.messengerdynamicfeature.composeapi"

        defaultConfig {
            compileSdk = libs.versions.android.compileSdk.get().toInt()
        }
    }

    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":messengerDynamicFeature:api"))
                implementation(libs.composeMultiplatform.runtime)
                implementation(compose.ui)
                implementation(compose.runtime)
            }
        }
    }

}


