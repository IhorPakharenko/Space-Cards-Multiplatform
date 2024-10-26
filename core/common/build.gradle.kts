plugins {
    alias(libs.plugins.yfoo3.kotlinMultiplatform)
    alias(libs.plugins.yfoo3.composeMultiplatform)
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.isao.yfoo3.resources"
    generateResClass = always
}