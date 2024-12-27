import org.gradle.api.Plugin
import org.gradle.api.Project

class ComposeMultiplatformConventionPlugin : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    configureComposeAndroid()
    configureComposeMultiplatform()
  }
}
