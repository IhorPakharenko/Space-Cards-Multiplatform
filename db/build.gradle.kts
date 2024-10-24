plugins {
    alias(libs.plugins.yfoo3.kotlinMultiplatform)
}

kotlin {
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    sourceSets {
        commonMain.dependencies {

        }
        androidMain.dependencies {

        }
        desktopMain.dependencies {

        }
        iosMain.dependencies {

        }
    }
}