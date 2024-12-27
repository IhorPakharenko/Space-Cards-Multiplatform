plugins {
  alias(libs.plugins.spacecards.kotlinMultiplatform)
  alias(libs.plugins.spacecards.composeMultiplatform)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(projects.core.designsystem)
      implementation(projects.core.model)
      implementation(projects.core.common)
      implementation(projects.core.domain)
    }
  }
}
