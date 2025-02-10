import org.gradle.api.Plugin
import org.gradle.api.Project

//TODO currently unused. Remove?
class ArrowOpticsConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    configureArrowOptics()
  }
}
