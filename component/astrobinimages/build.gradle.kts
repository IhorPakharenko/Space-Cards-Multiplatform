plugins {
  alias(libs.plugins.spacecards.kotlinMultiplatform)
  alias(libs.plugins.sqldelight)
  alias(libs.plugins.buildConfig)
  kotlin("plugin.serialization") version libs.versions.kotlin
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
      implementation(projects.foundation)
      implementation(projects.component.common)
      implementation(projects.pager)
      implementation(libs.ktor.client.core)
      implementation(libs.sqldelight.coroutines)
      // TODO should be available by default, but it is not
      implementation(libs.kotlinx.coroutines.core)
    }
  }
}

sqldelight {
  databases {
    create("Database") {
      packageName.set("com.isao.spacecards.component.astrobinimages.data")
    }
  }
}
