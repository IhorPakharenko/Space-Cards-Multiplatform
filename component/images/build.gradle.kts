plugins {
  alias(libs.plugins.spacecards.kotlinMultiplatform)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(projects.component.common)
      // TODO there might be a way to not include at least implementations of db and network here
      implementation(projects.core.db)
      implementation(projects.core.network)
      implementation(projects.core.sqldelight)
      implementation(projects.core.ktor)
    }
  }
}
