plugins {
    alias(libs.plugins.yfoo3.kotlinMultiplatform)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.model)
            implementation(projects.core.sqldelight)
        }
    }
}