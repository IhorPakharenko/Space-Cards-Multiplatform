plugins {
  alias(libs.plugins.spacecards.kotlinMultiplatform)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      //TODO leaking sqldelight into a common component module. Should we do that?
      implementation(projects.core.sqldelight)
      implementation(libs.paging.common)
    }
  }
}
