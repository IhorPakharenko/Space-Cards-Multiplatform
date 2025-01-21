import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
  //TODO uncommenting this line causes an extremely cryptic error:
  // Error resolving plugin [id: 'com.isao.spacecards.kotlinMultiplatform', version: 'unspecified']
  // > The request for this plugin could not be satisfied because the plugin is already on the classpath with an unknown version, so compatibility cannot be checked.
  // The only thing that this plugin does is formatting this single file with Spotless, so it's not a big deal.
//  alias(libs.plugins.spacecards.root)
  alias(libs.plugins.androidApplication) apply false
  alias(libs.plugins.androidLibrary) apply false
  alias(libs.plugins.jetbrainsCompose) apply false
  alias(libs.plugins.compose.compiler) apply false
  alias(libs.plugins.kotlinMultiplatform) apply false
  alias(libs.plugins.jetbrains.kotlin.jvm) apply false
  alias(libs.plugins.ksp) apply false
  kotlin("plugin.serialization") version "2.1.0" apply false
  alias(libs.plugins.buildConfig) apply false
  alias(libs.plugins.versions)
  alias(libs.plugins.versionCatalogUpdate)
  alias(libs.plugins.spotless)
}

tasks.withType<DependencyUpdatesTask> {
  rejectVersionIf {
    isNonStable(candidate.version) && !isNonStable(currentVersion)
  }
}

private fun isNonStable(version: String): Boolean {
  val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
  val regex = "^[0-9,.v-]+(-r)?$".toRegex()
  val isStable = stableKeyword || regex.matches(version)
  return isStable.not()
}
