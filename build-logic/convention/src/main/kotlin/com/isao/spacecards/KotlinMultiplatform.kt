package com.isao.spacecards

import configureSpotless
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.exclude
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
internal fun Project.configureKotlinMultiplatform(extension: KotlinMultiplatformExtension) =
  extension.apply {
    jvmToolchain(17)

    androidTarget {
      @OptIn(ExperimentalKotlinGradlePluginApi::class)
      compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
      }
    }

    jvm("desktop")

//    wasmJs { browser() }

    listOf(iosX64(), iosArm64(), iosSimulatorArm64())

    applyDefaultHierarchyTemplate()

    sourceSets.apply {
      commonMain {
        dependencies {
          implementation(libs.findLibrary("kotlinx.coroutines.core").get())
          implementation(libs.findLibrary("kotlinx.datetime").get())
          implementation(libs.findLibrary("kermit").get())

          // // Koin Libraries
          implementation(libs.findLibrary("koin.core").get())
          implementation(libs.findLibrary("koin.core.coroutines").get())
          api(libs.findLibrary("koin.annotations").get())

          implementation(libs.findLibrary("kermit.koin").get())

          implementation(libs.findLibrary("kotlinx.serialization.json").get())
        }
        // Apparently makes ksp-generated code visible
        commonMain.configure {
          kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
        }

        androidMain {
          dependencies {
//                    implementation(libs.findLibrary("koin.android").get())
            implementation(libs.findLibrary("kotlinx.coroutines.android").get())
          }
        }

        jvmMain.dependencies {
          implementation(libs.findLibrary("kotlinx.coroutines.swing").get())
        }

        jsMain.dependencies {
        }
      }
    }

    dependencies {
      add("kspCommonMainMetadata", libs.findLibrary("koin.ksp.compiler").get())
      add("kspAndroid", libs.findLibrary("koin.ksp.compiler").get())
      add("kspDesktop", libs.findLibrary("koin.ksp.compiler").get())
      add("kspIosX64", libs.findLibrary("koin.ksp.compiler").get())
      add("kspIosArm64", libs.findLibrary("koin.ksp.compiler").get())
      add("kspIosSimulatorArm64", libs.findLibrary("koin.ksp.compiler").get())
    }

//    extensions.configure<KspExtension> {
//        // Use KoinViewModel annotation with multiplatform support
//        arg("KOIN_USE_COMPOSE_VIEWMODEL", "true")
//    }

    // Trigger Common Metadata Generation from Native tasks
    project.tasks.withType(KotlinCompilationTask::class.java).configureEach {
      if (name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
      }
    }

    // Desktop depends on Android Coroutines and crashes otherwise
    configurations.named("desktopMainApi") {
      exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-android")
    }

    configureSpotless()
  }
