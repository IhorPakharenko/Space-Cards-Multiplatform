plugins {
  alias(libs.plugins.spacecards.kotlinMultiplatform)
  alias(libs.plugins.spacecards.composeMultiplatform)
}

compose.resources {
  publicResClass = true
  packageOfResClass = "com.isao.spacecards.resources"
  generateResClass = always
}
