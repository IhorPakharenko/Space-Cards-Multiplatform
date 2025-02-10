import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.androidLibrary)
}
kotlin {
  jvmToolchain(17)

  androidTarget {
    compilerOptions {
      jvmTarget.set(JvmTarget.JVM_17)
    }
  }

  jvm()

  listOf(iosX64(), iosArm64(), iosSimulatorArm64())

  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.arrow.core)
      }
    }
  }
}

android {
  namespace = "isao.pager"
  compileSdk = 35
  defaultConfig {
    minSdk = 21
  }
}
