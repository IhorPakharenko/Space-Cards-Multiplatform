plugins {
  alias(libs.plugins.spacecards.kotlinMultiplatform)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(projects.core.data)
      implementation(projects.core.common)
    }
  }
}
dependencies {
}
