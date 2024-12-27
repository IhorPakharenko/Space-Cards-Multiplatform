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

      implementation(libs.coil.compose)
    }
  }
}
dependencies {
}

// TODO extract to a common place
ksp {
  arg("KOIN_USE_COMPOSE_VIEWMODEL", "true")
}
