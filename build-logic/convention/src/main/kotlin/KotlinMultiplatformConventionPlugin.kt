import com.isao.spacecards.configureKotlinAndroid
import com.isao.spacecards.configureKotlinMultiplatform
import org.gradle.api.Plugin
import org.gradle.api.Project

class KotlinMultiplatformConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    configureKotlinAndroid()
    configureKotlinMultiplatform()
    configureKsp()
    configureSpotless()
  }
}
