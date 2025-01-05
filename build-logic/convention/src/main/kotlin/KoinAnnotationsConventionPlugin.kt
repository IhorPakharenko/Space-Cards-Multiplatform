import org.gradle.api.Plugin
import org.gradle.api.Project

class KoinAnnotationsConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    configureKoinAnnotations()
  }
}
