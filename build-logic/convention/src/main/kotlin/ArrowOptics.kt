import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
internal fun Project.configureArrowOptics() {
  with(pluginManager) {
    apply(libs.findPlugin("ksp").get().get().pluginId)
  }

  kotlinMultiplatform {
    dependencies {
      add("kspCommonMainMetadata", libs.findPlugin("arrow-opticsPlugin").get().get().pluginId)
    }
    sourceSets.apply {
      commonMain.dependencies {
        implementation(libs.findLibrary("arrow-optics").get())
      }
    }
  }
}
