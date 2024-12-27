plugins {
  alias(libs.plugins.yfoo3.kotlinMultiplatform)
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
