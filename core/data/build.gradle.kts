plugins {
  alias(libs.plugins.spacecards.kotlinMultiplatform)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(projects.core.model)
      implementation(projects.core.db)
      implementation(projects.core.network)
    }
  }
}
