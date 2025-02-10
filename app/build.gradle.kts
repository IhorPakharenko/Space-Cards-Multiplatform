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
      implementation(projects.core.ktor)
      implementation(projects.feature.feed)
      implementation(projects.feature.liked)
      implementation(projects.feature.common)
      implementation(projects.component.astrobinimages)
      implementation(libs.sqldelight.primitiveAdapters)
      // TODO try moving these dependencies and code relying on them into designsystem module
      implementation(compose.material3AdaptiveNavigationSuite)
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
      dependency(projects.component.astrobinimages)
    }
  }
}

// How to run tests
// https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-test.html#z4y19y3_55
