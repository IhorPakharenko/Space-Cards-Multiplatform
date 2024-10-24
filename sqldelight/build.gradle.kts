plugins {
//    id("java-library")
//    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.yfoo3.kotlinMultiplatform)
    alias(libs.plugins.sqldelight)
}

//java {
//    sourceCompatibility = JavaVersion.VERSION_11
//    targetCompatibility = JavaVersion.VERSION_11
//}

kotlin {
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
        commonMain.dependencies {
            api(projects.db)
            implementation("app.cash.sqldelight:coroutines-extensions:2.0.2")
        }
        androidMain.dependencies {
            implementation("app.cash.sqldelight:android-driver:2.0.2")
        }
        desktopMain.dependencies {
            implementation("app.cash.sqldelight:sqlite-driver:2.0.2")
        }
        iosMain.dependencies {
            implementation("app.cash.sqldelight:native-driver:2.0.2")
        }
//        jsMain.dependencies {
//
//        }
    }
//    compilerOptions {
//        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
//    }
}

sqldelight {
    databases {
        create("Database") {
            packageName.set("com.isao.yfoo3.data")
        }
    }
}