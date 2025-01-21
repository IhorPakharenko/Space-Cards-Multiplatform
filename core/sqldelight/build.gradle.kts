plugins {
  alias(libs.plugins.spacecards.kotlinMultiplatform)
  alias(libs.plugins.sqldelight)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(projects.core.db)
      implementation(libs.sqldelight.coroutines)
    }
  }
}

sqldelight {
  databases {
    create("Database") {
      packageName.set("com.isao.spacecards.core.data")
    }
  }
}
