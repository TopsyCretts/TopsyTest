rootProject.name = "TopsyTest"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(":composeApp")

include(":messengerDynamicFeature:api")
project(":messengerDynamicFeature:api").projectDir = file("messengerDynamicFeature/api")

include(":messengerDynamicFeature:messengerImpl")
project(":messengerDynamicFeature:messengerImpl").projectDir = file("messengerDynamicFeature/messengerImpl")

include(":messengerDynamicFeature:composeapi")
project(":messengerDynamicFeature:composeapi").projectDir = file("messengerDynamicFeature/compose-api")
