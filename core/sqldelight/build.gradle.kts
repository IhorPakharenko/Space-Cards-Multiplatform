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
    androidMain.dependencies {
      implementation(libs.sqldelight.android)
    }
    jvmMain.dependencies {
      implementation(libs.sqldelight.sqlite)
    }
    iosMain.dependencies {
      implementation(libs.sqldelight.native)
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
