plugins {
  alias(libs.plugins.spacecards.kotlinMultiplatform)
  alias(libs.plugins.spacecards.composeMultiplatform)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(projects.feature.common)
      implementation(projects.feature.designsystem)
      implementation(projects.component.images)
    }
  }
}
