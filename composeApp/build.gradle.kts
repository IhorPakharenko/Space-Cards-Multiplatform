import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)

    alias(libs.plugins.junit)
//    alias(libs.plugins.kotlin)
    alias(libs.plugins.ksp)
//    alias(libs.plugins.kotlin.parcelize)
    id("kotlin-parcelize") // add this
    id("kotlin-kapt") // add this
    alias(libs.plugins.room)

//    id("com.google.devtools.ksp")
//    alias(libs.plugins.screenshot)
}

kotlin {
//    @OptIn(ExperimentalWasmDsl::class)
//    wasmJs {
//        moduleName = "composeApp"
//        browser {
//            val rootDirPath = project.rootDir.path
//            val projectDirPath = project.projectDir.path
//            commonWebpackConfig {
//                outputFileName = "composeApp.js"
//                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
//                    static = (static ?: mutableListOf()).apply {
//                        // Serve sources to debug inside browser
//                        add(rootDirPath)
//                        add(projectDirPath)
//                    }
//                }
//            }
//        }
//        binaries.executable()
//    }
    
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    jvm("desktop")
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        val desktopMain by getting
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
            implementation(libs.androidx.core.splashscreen)
            implementation(libs.ktor.client.android)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
////            implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:2.8.3")
            implementation(libs.androidx.lifecycle.runtime.compose)


//            implementation(libs.androidx.core.ktx)
//            implementation(libs.androidx.lifecycle.runtime)
////            implementation(libs.androidx.lifecycle.runtime.compose)
//            implementation(libs.compose.ui)
//            implementation(libs.compose.animation)
//            implementation(libs.compose.ui.tooling.preview)
//            implementation(libs.compose.material3)
//            implementation(libs.compose.material)
//            implementation(libs.compose.material.icons.extended)
//            implementation(libs.coil.compose)
//            implementation("io.coil-kt.coil3:coil:3.0.0-rc01")
//            implementation("io.coil-kt.coil3:coil-network-ktor3:3.0.0-rc01")
            implementation(libs.ktor.client.core)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)
////            implementation(libs.accompanist.navigation.animation)
            implementation(libs.accompanist.placeholder.material)
            implementation(libs.accompanist.drawablepainter)

            //// Room Libraries
            implementation(libs.room.runtime)
            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)
//            implementation(libs.room.ktx)
////            ksp(libs.room.compiler)

            //// Koin Libraries
            implementation(libs.koin.core)
            implementation(libs.koin.core.coroutines)
            implementation(libs.koin.compose.viewmodel)
////            implementation(libs.koin.compose)
////            implementation(libs.koin.compose.navigation)
            implementation("org.jetbrains.androidx.navigation:navigation-compose:2.7.0-alpha07")
            api(libs.koin.annotations)
////            ksp(libs.koin.ksp.compiler)

            //// Other Libraries
            implementation(libs.kotlinx.serialization.json)
////            implementation(libs.retrofit.kotlinx.serialization.converter)
//            implementation(libs.okhttp.logging.interceptor)
//            implementation(libs.timber)
////            implementation(libs.navigation.compose)
////            coreLibraryDesugaring(libs.desugar.jdk.libs)
////            lintChecks(libs.compose.lint.checks)
            implementation("co.touchlab:kermit:2.0.4")
            implementation("co.touchlab:kermit-koin:2.0.4")
            implementation(compose.materialIconsExtended)
            implementation(libs.ktor.client.core)
            implementation(libs.coil.network.ktor)
            implementation(libs.koin.compose)
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")

        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.ktor.client.java)

        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }

        // KSP Common sourceSet
        sourceSets.named("commonMain").configure {
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
        }
    }
}

android {
    namespace = "com.isao.yfoo3"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.isao.yfoo3"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.ui.tooling.preview.android)
    debugImplementation(compose.uiTooling)
//    debugImplementation(libs.compose.ui.tooling) obsolete?
//    debugImplementation(libs.compose.ui.test.manifest) do these work?
//    androidTestImplementation(libs.compose.ui.test.junit4) do these work?
//    add("kspCommonMainMetadata", project(":composeApp"))
//    add("kspJvm", project(":composeApp"))

    add("kspCommonMainMetadata", libs.koin.ksp.compiler)
    add("kspAndroid", libs.koin.ksp.compiler)
    add("kspDesktop", libs.koin.ksp.compiler)
    add("kspIosX64", libs.koin.ksp.compiler)
    add("kspIosArm64", libs.koin.ksp.compiler)
    add("kspIosSimulatorArm64", libs.koin.ksp.compiler)

    add("kspAndroid", libs.room.compiler)
    add("kspDesktop", libs.room.compiler)
    add("kspIosSimulatorArm64", libs.room.compiler)
    add("kspIosX64", libs.room.compiler)
    add("kspIosArm64", libs.room.compiler)
}

// Trigger Common Metadata Generation from Native tasks
project.tasks.withType(KotlinCompilationTask::class.java).configureEach {
    if(name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}

// Desktop depends on Android Coroutines and crashes otherwise
configurations.named("desktopMainApi") {
    exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-android")
}

room {
    schemaDirectory("$projectDir/schemas")
}

// Use KoinViewModel annotation with multiplatform support
ksp {
    arg("KOIN_USE_COMPOSE_VIEWMODEL","true")
}

compose.desktop {
    application {
        mainClass = "com.isao.yfoo3.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.isao.yfoo3"
            packageVersion = "1.0.0"
        }
    }
}
