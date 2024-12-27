import com.isao.spacecards.composeCompiler
import com.isao.spacecards.ksp
import com.isao.spacecards.libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun Project.configureComposeMultiplatform() {
  with(pluginManager) {
    apply(libs.findPlugin("jetbrainsCompose").get().get().pluginId)
    apply(libs.findPlugin("compose.compiler").get().get().pluginId)
  }

  val composeDeps = extensions.getByType<ComposeExtension>().dependencies

  extensions.configure<KotlinMultiplatformExtension> {
    sourceSets.apply {
      commonMain.dependencies {
        implementation(composeDeps.runtime)
        implementation(composeDeps.foundation)
        implementation(composeDeps.material3)
        implementation(composeDeps.materialIconsExtended)
        implementation(composeDeps.material)
        implementation(composeDeps.ui)
        implementation(composeDeps.components.resources)
        implementation(composeDeps.components.uiToolingPreview)

        implementation(libs.findLibrary("androidx.lifecycle.viewmodel").get())
        implementation(libs.findLibrary("androidx.lifecycle.runtime.compose").get())
        implementation(libs.findLibrary("jetbrains.navigation.compose").get())
        implementation(libs.findLibrary("koin.compose.viewmodel").get())
        implementation(libs.findLibrary("koin.compose").get())
        implementation(libs.findLibrary("multiplatform.material3.window.size").get())
        implementation(libs.findLibrary("coil.compose").get())
        implementation(libs.findLibrary("coil.network.ktor").get())
        implementation(libs.findLibrary("ktor.client.core").get())
      }
      androidMain.dependencies {
        implementation(composeDeps.preview)
      }
      jvmMain.dependencies {
        implementation(composeDeps.desktop.currentOs)
      }
    }
  }

  ksp {
    arg("KOIN_USE_COMPOSE_VIEWMODEL", "true")
  }

  composeCompiler {
    stabilityConfigurationFiles = listOf(
      rootProject.layout.projectDirectory.file("compose_stability.conf"),
    )
  }
}
