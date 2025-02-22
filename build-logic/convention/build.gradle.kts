plugins {
  `kotlin-dsl`
  alias(libs.plugins.spotless)
}

group = "com.isao.spacecards.buildlogic"

dependencies {
  compileOnly(libs.plugins.androidApplication.toDep())
  compileOnly(libs.plugins.androidLibrary.toDep())
  compileOnly(libs.plugins.jetbrainsCompose.toDep())
  compileOnly(libs.plugins.kotlinMultiplatform.toDep())
  compileOnly(libs.plugins.compose.compiler.toDep())
  compileOnly(libs.plugins.ksp.toDep())
  compileOnly(libs.plugins.spotless.toDep())
  compileOnly(libs.plugins.buildConfig.toDep())
}

fun Provider<PluginDependency>.toDep() = map {
  "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}"
}

spotless {
  val ktlintVersion = libs.versions.ktlint.get()
  kotlin {
    target("src/**/*.kt")
    ktlint(ktlintVersion)
  }

  kotlinGradle {
    target("*.kts")
    ktlint(ktlintVersion)
  }
}

gradlePlugin {
  plugins {
    register("root") {
      id = "com.isao.spacecards.root"
      implementationClass = "RootConventionPlugin"
    }
    register("kotlinMultiplatform") {
      id = "com.isao.spacecards.kotlinMultiplatform"
      implementationClass = "KotlinMultiplatformConventionPlugin"
    }
    register("composeMultiplatform") {
      id = "com.isao.spacecards.composeMultiplatform"
      implementationClass = "ComposeMultiplatformConventionPlugin"
    }
    register("koinAnnotations") {
      id = "com.isao.spacecards.koinAnnotations"
      implementationClass = "KoinAnnotationsConventionPlugin"
    }
    register("arrowOptics") {
      id = "com.isao.spacecards.arrowOptics"
      implementationClass = "ArrowOpticsConventionPlugin"
    }
  }
}
