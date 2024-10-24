plugins {
    `kotlin-dsl`
}

group = "com.isao.yfoo3.buildlogic"

dependencies {
    compileOnly(libs.plugins.androidApplication.toDep())
    compileOnly(libs.plugins.androidLibrary.toDep())
    compileOnly(libs.plugins.jetbrainsCompose.toDep())
    compileOnly(libs.plugins.kotlinMultiplatform.toDep())
    compileOnly(libs.plugins.compose.compiler.toDep())
    compileOnly(libs.plugins.ksp.toDep())
}

fun Provider<PluginDependency>.toDep() = map {
    "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}"
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