plugins {
  alias(libs.plugins.spacecards.kotlinMultiplatform)
  alias(libs.plugins.spacecards.composeMultiplatform)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(projects.foundation)
      implementation(projects.feature.common)
      implementation(projects.feature.designsystem)
      implementation(projects.component.astrobinimages)
      implementation(projects.pager)
      implementation(libs.kotlinx.coroutines.core)
    }
  }
}
