plugins {
  alias(libs.plugins.spacecards.kotlinMultiplatform)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(projects.foundation)
      implementation(libs.ktor.client.core)
    }
  }
}
