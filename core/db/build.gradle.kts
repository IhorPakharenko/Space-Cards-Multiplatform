plugins {
  alias(libs.plugins.spacecards.kotlinMultiplatform)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(projects.core.model)
    }
    androidMain.dependencies {
    }
    desktopMain.dependencies {
    }
    iosMain.dependencies {
    }
  }
}
