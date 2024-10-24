package com.isao.yfoo3

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
internal fun Project.configureKotlinMultiplatform(
    extension: KotlinMultiplatformExtension
) = extension.apply {
    jvmToolchain(17)

    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    jvm("desktop")

//    wasmJs { browser() }

    listOf(iosArm64(), iosSimulatorArm64())

    applyDefaultHierarchyTemplate()

    sourceSets.apply {
        commonMain {
            dependencies {
                implementation(libs.findLibrary("kotlinx.coroutines.core").get())
                implementation(libs.findLibrary("kotlinx.datetime").get())
                api(libs.findLibrary("koin.core").get())
                api(libs.findLibrary("koin.annotations").get())
//                implementation(libs.findLibrary("kermit").get())
            }

            androidMain {
                dependencies {
//                    implementation(libs.findLibrary("koin.android").get())
                    implementation(libs.findLibrary("kotlinx.coroutines.android").get())
                }

                jvmMain.dependencies {
                    implementation(libs.findLibrary("kotlinx.coroutines.swing").get())
                }
            }
        }
    }

    dependencies {
//        add("kspCommonMainMetadata", libs.findLibrary("koin.ksp.compiler"))
//        add("kspAndroid", libs.findLibrary("koin.ksp.compiler"))
//        add("kspDesktop", libs.findLibrary("koin.ksp.compiler"))
//        add("kspIosX64", libs.findLibrary("koin.ksp.compiler"))
//        add("kspIosArm64", libs.findLibrary("koin.ksp.compiler"))
//        add("kspIosSimulatorArm64", libs.findLibrary("koin.ksp.compiler"))

//        add("kspAndroid", libs.findLibrary("koin.ksp.compiler"))
//        add("kspDesktop", libs.findLibrary("koin.ksp.compiler"))
//        add("kspIosSimulatorArm64", libs.findLibrary("koin.ksp.compiler"))
//        add("kspIosX64", libs.findLibrary("koin.ksp.compiler"))
//        add("kspIosArm64", libs.findLibrary("koin.ksp.compiler"))
    }
}