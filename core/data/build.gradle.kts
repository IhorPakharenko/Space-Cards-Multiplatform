plugins {
  alias(libs.plugins.spacecards.kotlinMultiplatform)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.core.model)
      implementation(projects.core.sqldelight)
    }
  }
}

dependencies {
}
