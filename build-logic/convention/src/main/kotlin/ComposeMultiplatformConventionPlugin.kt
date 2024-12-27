import com.isao.spacecards.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class ComposeMultiplatformConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    with(pluginManager) {
      apply(libs.findPlugin("jetbrainsCompose").get().get().pluginId)
      apply(libs.findPlugin("compose.compiler").get().get().pluginId)
    }

    val composeDeps = extensions.getByType<ComposeExtension>().dependencies

    extensions.configure<KotlinMultiplatformExtension> {
      sourceSets.apply {
        commonMain {
          dependencies {
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

            implementation(
              libs.findLibrary("multiplatform.material3.window.size").get(),
            )
          }
        }
      }
    }
  }
}
