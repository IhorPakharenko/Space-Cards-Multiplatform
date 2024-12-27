plugins {
    `kotlin-dsl`
    alias(libs.plugins.spotless)
}

group = "com.isao.yfoo3.buildlogic"

dependencies {
    compileOnly(libs.plugins.androidApplication.toDep())
    compileOnly(libs.plugins.androidLibrary.toDep())
    compileOnly(libs.plugins.jetbrainsCompose.toDep())
    compileOnly(libs.plugins.kotlinMultiplatform.toDep())
    compileOnly(libs.plugins.compose.compiler.toDep())
    compileOnly(libs.plugins.ksp.toDep())
    compileOnly(libs.plugins.spotless.toDep())
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
        register("kotlinMultiplatform") {
            id = "com.isao.yfoo3.kotlinMultiplatform"
            implementationClass = "KotlinMultiplatformConventionPlugin"
        }
        register("composeMultiplatform") {
            id = "com.isao.yfoo3.composeMultiplatform"
            implementationClass = "ComposeMultiplatformConventionPlugin"
        }
    }
}
