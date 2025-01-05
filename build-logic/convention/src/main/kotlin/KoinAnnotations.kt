import org.gradle.api.Project

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
internal fun Project.configureKoinAnnotations() {
  configureKsp()

  kotlinMultiplatform {
    sourceSets.apply {
      commonMain.dependencies {
        implementation(libs.findLibrary("koin.annotations").get())
      }
    }
  }

  ksp {
    arg("KOIN_USE_COMPOSE_VIEWMODEL", "true")
  }
}
