import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree
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
//    id("kotlin-kapt") // add this
    alias(libs.plugins.room)

    alias(libs.plugins.screenshot)
    alias(libs.plugins.kotest.multiplatform)

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
//        @OptIn(ExperimentalKotlinGradlePluginApi::class)
//        compilerOptions {
//            jvmTarget.set(JvmTarget.JVM_11)
//        }
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
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
        val desktopTest by getting
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
            implementation(libs.androidx.core.splashscreen)
            implementation(libs.ktor.client.android)
            implementation("com.squareup.sqldelight:android-driver:1.5.5")
//            screenshotTestImplementation(libs.compose.ui.tooling)
        }
        commonMain.dependencies {
            implementation(projects.sqldelight)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.ktor.client.core)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)
            implementation(libs.accompanist.placeholder.material)
            implementation(libs.accompanist.drawablepainter)

            //// Room Libraries
            implementation(libs.room.runtime)
            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)

            //// Koin Libraries
            implementation(libs.koin.core)
            implementation(libs.koin.core.coroutines)
            implementation(libs.koin.compose.viewmodel)
            implementation("org.jetbrains.androidx.navigation:navigation-compose:2.7.0-alpha07")
            api(libs.koin.annotations)

            //// Other Libraries
            implementation(libs.kotlinx.serialization.json)
            implementation("co.touchlab:kermit:2.0.4")
            implementation("co.touchlab:kermit-koin:2.0.4")
            implementation(compose.materialIconsExtended)
            implementation(libs.ktor.client.core)
            implementation(libs.coil.network.ktor)
            implementation(libs.koin.compose)
            implementation(libs.kotlinx.datetime)

            implementation("com.squareup.sqldelight:runtime:1.5.5")
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.ktor.client.java)

        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation("com.squareup.sqldelight:native-driver:1.5.5")
        }
//        jsMain.dependencies {
//            implementation("com.squareup.sqldelight:sqljs-driver:1.5.5")
//        }

        commonTest.dependencies {
            implementation(libs.kotest.assertions.core)
            implementation(libs.kotest.framework.engine)
            implementation(libs.kotest.framework.datatest)
            implementation(libs.kotest.extensions.koin)
            implementation(libs.koin.test)
            implementation(libs.turbine)

            implementation(kotlin("test"))
            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)
        }
        desktopTest.dependencies { //TODO is this the same as desktopTest?
            implementation(libs.mockk)
            implementation(libs.koin.test.junit5)
            implementation(libs.kotest.runner.junit5)
            implementation(compose.desktop.currentOs)
        }
        androidUnitTest.dependencies {
            implementation(libs.mockk.android)
            implementation(libs.mockk.agent)
            implementation(libs.kotest.extensions.android)
            implementation(libs.kotest.runner.android)
        }
        androidInstrumentedTest.dependencies {
            implementation(libs.kotest.runner.android)
            implementation(libs.koin.test)
//            implementation(libs.androidx.test.ext.junit)
//            implementation(libs.androidx.test.espresso.core)
            implementation(libs.kotest.assertions.android)
            implementation(libs.compose.ui.test.junit4)
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

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    experimentalProperties["android.experimental.enableScreenshotTest"] = true

    dependencies { //TODO is this needed?
        screenshotTestImplementation(libs.compose.ui.tooling)
    }

    sourceSets.all {
        java.srcDirs("src/$name/kotlin")
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
    tasks.withType<Test> {
        testLogging { // Enables println() in unit tests
            events("standardOut")
        }
    }
}

composeCompiler {
    stabilityConfigurationFile = File(projectDir, "compose_stability.conf") //TODO check if it works
}

dependencies {
    implementation(libs.androidx.ui.tooling.preview.android)
    debugImplementation(compose.uiTooling)

    androidTestImplementation("androidx.compose.ui:ui-test-junit4-android:1.7.4")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.7.4")
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

//sqldelight {
//    databases {
//        create("Database") {
//            packageName.set("com.isao.yfoo3.data")
//        }
//    }
//}

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

tasks.named<Test>("desktopTest") {
    useJUnitPlatform()
//    filter {
//        isFailOnNoMatchingTests = false
//    }
    testLogging {
        showExceptions = true
        showStandardStreams = true
        events = setOf(
            org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
        )
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

// How to run tests
// https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-test.html#z4y19y3_55
