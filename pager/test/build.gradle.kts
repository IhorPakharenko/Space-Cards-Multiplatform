plugins {
  alias(libs.plugins.spacecards.kotlinMultiplatform)
  alias(libs.plugins.kotest.multiplatform)
}

kotlin {
  sourceSets {
    commonTest.dependencies {
      implementation(projects.pager)
      implementation(libs.turbine)
      implementation(libs.kotest.assertions.core)
      implementation(libs.kotest.framework.engine)
    }
    jvmTest.dependencies {
      implementation(libs.kotest.runner.junit5)
    }
  }
}

tasks.withType<Test>().configureEach {
  useJUnitPlatform()
}
