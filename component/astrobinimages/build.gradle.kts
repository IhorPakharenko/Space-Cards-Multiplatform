plugins {
  alias(libs.plugins.spacecards.kotlinMultiplatform)
  alias(libs.plugins.sqldelight)
  alias(libs.plugins.buildConfig)
  kotlin("plugin.serialization") version "2.1.0" //TODO add to toml
}

buildConfig {
  packageName = "com.isao.spacecards.component.astrobinimages"

  with(Secrets(project, this)) {
    buildConfigField(Secrets.Key.ASTROBIN_API_KEY)
    buildConfigField(Secrets.Key.ASTROBIN_API_SECRET)
  }
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(projects.component.common)
      implementation(projects.core.sqldelight)
      implementation(libs.ktor.client.core)
      implementation(libs.sqldelight.coroutines)
      implementation(libs.store)
      implementation(libs.store.paging)
      implementation(libs.kotlinx.coroutines.core)
      implementation(libs.paging.common)
    }
  }
}

sqldelight {
  databases {
    create("Database") {
      packageName.set("com.isao.spacecards.astrobinimages.data")
    }
  }
}
