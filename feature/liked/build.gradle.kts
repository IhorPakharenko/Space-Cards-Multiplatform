plugins {
    alias(libs.plugins.yfoo3.kotlinMultiplatform)
    alias(libs.plugins.yfoo3.composeMultiplatform)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.designsystem)
            implementation(projects.core.model)
            implementation(projects.core.common)
            implementation(projects.core.domain)

            implementation(libs.coil.compose)
        }
    }
}
dependencies {

}