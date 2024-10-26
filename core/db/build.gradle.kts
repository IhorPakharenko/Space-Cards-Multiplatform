plugins {
    alias(libs.plugins.yfoo3.kotlinMultiplatform)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.model)
        }
        androidMain.dependencies {

        }
        desktopMain.dependencies {

        }
        iosMain.dependencies {

        }
    }
}