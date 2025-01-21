import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
  alias(libs.plugins.androidApplication)
  alias(libs.plugins.spacecards.kotlinMultiplatform)
  alias(libs.plugins.spacecards.composeMultiplatform)
  alias(libs.plugins.sqldelight)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(projects.feature.designsystem)
      implementation(projects.core.sqldelight)
      implementation(projects.core.ktor)
      implementation(projects.feature.feed)
      implementation(projects.feature.liked)
      implementation(projects.feature.common)
      implementation(projects.component.images)
      implementation(projects.core.db)
      implementation(projects.component.astrobinimages)
      implementation(libs.sqldelight.coroutines)
      implementation(libs.sqldelight.primitiveAdapters)
    }
    androidMain.dependencies {
      implementation(libs.androidx.core.splashscreen)
      implementation(libs.sqldelight.android)
    }
    jvmMain.dependencies {
      implementation(libs.sqldelight.sqlite)
    }
    iosMain.dependencies {
      implementation(libs.sqldelight.native)
    }

    // TODO tests for a multi module project.
    // TODO These dependencies need to be filtered and added to the correct module.
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
    jvmTest.dependencies {
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
      implementation(libs.kotest.assertions.android)
      implementation(libs.compose.ui.test.junit4)
    }
  }
}

compose.desktop {
  application {
    mainClass = "com.isao.spacecards.MainKt"

    nativeDistributions {
      targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
      packageName = "com.isao.spacecards"
      packageVersion = "1.0.0"
    }
  }
}

sqldelight {
  databases {
    create("Database") {
      packageName.set("com.isao.spacecards.app.data")
      dependency(projects.core.sqldelight)
      dependency(projects.component.astrobinimages)
    }
  }
}

tasks.named<Test>("jvmTest") {
  useJUnitPlatform()
  testLogging {
    showExceptions = true
    showStandardStreams = true
    events = setOf(
      org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
      org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
    )
    exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
  }
}

// How to run tests
// https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-test.html#z4y19y3_55
