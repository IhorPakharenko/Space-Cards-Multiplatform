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

include(":app")
include(":foundation")
include(":core")
include(":core:ktor")
include(":feature")
include(":feature:common")
include(":feature:designsystem")
include(":feature:feed")
include(":feature:liked")
include(":component")
include(":component:common")
include(":component:astrobinimages")
include(":pager")
include(":pager:test")
