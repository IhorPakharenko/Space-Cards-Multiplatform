import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
internal fun Project.configureKoinAnnotations() {
  with(pluginManager) {
    apply(libs.findPlugin("ksp").get().get().pluginId)
  }

  kotlinMultiplatform {
    sourceSets.apply {
      commonMain {
        // Apparently makes ksp-generated code visible
        commonMain.configure {
          kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
        }
      }
    }

    dependencies {
      add("kspCommonMainMetadata", libs.findLibrary("koin.ksp.compiler").get())
      add("kspAndroid", libs.findLibrary("koin.ksp.compiler").get())
      add("kspJvm", libs.findLibrary("koin.ksp.compiler").get())
      add("kspIosX64", libs.findLibrary("koin.ksp.compiler").get())
      add("kspIosArm64", libs.findLibrary("koin.ksp.compiler").get())
      add("kspIosSimulatorArm64", libs.findLibrary("koin.ksp.compiler").get())
    }

    // Trigger Common Metadata Generation from Native tasks
    project.tasks.withType(KotlinCompilationTask::class.java).configureEach {
      if (name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
      }
    }

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
}
