package com.isao.spacecards

import org.gradle.api.Project
import org.gradle.kotlin.dsl.exclude
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
internal fun Project.configureKotlinMultiplatform() {
  with(pluginManager) {
    apply(libs.findPlugin("kotlinMultiplatform").get().get().pluginId)
  }

  kotlinMultiplatform {
    jvmToolchain(17)

    androidTarget {
      compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
      }
    }

    jvm()

    listOf(iosX64(), iosArm64(), iosSimulatorArm64())

    applyDefaultHierarchyTemplate()

    sourceSets.apply {
      commonMain.dependencies {
        implementation(libs.findLibrary("kotlinx.coroutines.core").get())
        implementation(libs.findLibrary("kotlinx.datetime").get())
        implementation(libs.findLibrary("kotlinx.serialization.json").get())

        implementation(libs.findLibrary("koin.core").get())
        implementation(libs.findLibrary("koin.core.coroutines").get())
        api(libs.findLibrary("koin.annotations").get())

        implementation(libs.findLibrary("kermit").get())
        implementation(libs.findLibrary("kermit.koin").get())
      }
      androidMain.dependencies {
        implementation(libs.findLibrary("koin.android").get())
        implementation(libs.findLibrary("kotlinx.coroutines.android").get())
        implementation(libs.findLibrary("ktor.client.android").get())
      }
      jvmMain.dependencies {
        implementation(libs.findLibrary("kotlinx.coroutines.swing").get())
        implementation(libs.findLibrary("ktor.client.java").get())
      }
      iosMain.dependencies {
        implementation(libs.findLibrary("ktor.client.darwin").get())
      }
    }

    // Desktop depends on Android Coroutines and crashes otherwise
    configurations.named("jvmMainApi") {
      exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-android")
    }
  }
}
