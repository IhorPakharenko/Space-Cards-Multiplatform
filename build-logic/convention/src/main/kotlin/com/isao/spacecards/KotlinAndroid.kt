package com.isao.spacecards

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.withType

internal fun Project.configureKotlinAndroid(extension: LibraryExtension) = extension.apply {
  // get module name from module path
  val moduleName = path.split(":").drop(2).joinToString(".")
  namespace =
    if (moduleName.isNotEmpty()) "com.isao.spacecards.$moduleName" else "com.isao.spacecards"

  compileSdk = libs.findVersion("android.compileSdk").get().requiredVersion.toInt()
  defaultConfig {
    minSdk = libs.findVersion("android.minSdk").get().requiredVersion.toInt()
    consumerProguardFiles("consumer-proguard-rules.pro")
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
  experimentalProperties["android.experimental.enableScreenshotTest"] = true
  sourceSets.all {
    java.srcDirs("src/$name/kotlin")
  }
  testOptions {
    unitTests {
      isIncludeAndroidResources = true
    }
  }
  tasks.withType<Test> {
    testLogging {
      // Enables println() in unit tests
      events("standardOut")
    }
  }
}
