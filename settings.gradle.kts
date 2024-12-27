rootProject.name = "SpaceCards"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
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
//include(":build-logic")
//include(":build-logic:convention")
include(":core")
include(":core:model")
include(":core:db")
include(":core:sqldelight")
include(":core:network")
include(":core:ktor")
include(":core:domain")
include(":core:common")
include(":core:data")
include(":core:designsystem")
include(":feature")
include(":feature:feed")
include(":feature:liked")
