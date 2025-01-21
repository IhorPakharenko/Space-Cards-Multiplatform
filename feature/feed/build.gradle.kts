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
      implementation(projects.component.astrobinimages)
      implementation(libs.store)
      implementation(libs.store.paging)
      implementation(libs.kotlinx.coroutines.core)
      implementation(libs.paging.common)
      implementation(libs.paging.compose.common)
    }
  }
}
