plugins {
    alias(libs.plugins.yfoo3.kotlinMultiplatform)
    alias(libs.plugins.yfoo3.composeMultiplatform)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
        }
    }
}