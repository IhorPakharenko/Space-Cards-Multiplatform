plugins {
  alias(libs.plugins.yfoo3.kotlinMultiplatform)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(projects.core.model)
      implementation(projects.core.common)

      implementation(libs.ktor.client.core)
      implementation(libs.ktor.client.content.negotiation)
      implementation(libs.ktor.serialization.kotlinx.json)
    }
    androidMain.dependencies {
      implementation(libs.ktor.client.android)
    }
    desktopMain.dependencies {
      implementation(libs.ktor.client.java)
    }
    iosMain.dependencies {
      implementation(libs.ktor.client.darwin)
    }
    jsMain.dependencies {
      implementation(libs.ktor.client.js)
    }
  }
}
