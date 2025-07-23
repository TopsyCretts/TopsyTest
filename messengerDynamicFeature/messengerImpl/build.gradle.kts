plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidDynamicFeature)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

val tdApiHash:String by project
val tdApiId:String by project

android{
    namespace = "com.top.messengerdynamicfeature.messengerImpl"

    sourceSets["main"].jniLibs.srcDirs("src/androidMain/jniLibs")
    sourceSets["main"].java.srcDirs("src/androidMain/java", "src/androidMain/kotlin")

    buildFeatures{
        buildConfig = true
    }

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "TDLIB_API_HASH", "\"$tdApiHash\"")
        buildConfigField("String", "TDLIB_API_ID", "\"$tdApiId\"")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
kotlin {

    jvmToolchain(11)

    iosX64()
    iosArm64()
    iosSimulatorArm64()
    androidTarget()

    sourceSets {
        commonMain {
            dependencies {

                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)

                implementation(libs.kotlin.stdlib)
                implementation(libs.composeMultiplatform.runtime)
                implementation(libs.decompose)
                implementation(libs.decomposeComposeExtension)

                implementation(libs.bundles.koin)

                implementation(project(":messengerDynamicFeature:api"))
                implementation(project(":messengerDynamicFeature:composeapi"))
            }
        }

        androidMain {
            dependencies {
                implementation(project(":composeApp"))
            }
        }
    }

}