import com.android.build.api.dsl.LibraryExtension
import com.isao.spacecards.configureKotlinAndroid
import com.isao.spacecards.configureKotlinMultiplatform
import com.isao.spacecards.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KotlinMultiplatformConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    with(pluginManager) {
      apply(libs.findPlugin("kotlinMultiplatform").get().get().pluginId)
      apply(libs.findPlugin("androidLibrary").get().get().pluginId)
      apply(libs.findPlugin("ksp").get().get().pluginId)
//            apply("com.diffplug.spotless")
//            apply(libs.findPlugin("kotlin.serialization").get().get().pluginId)
    }

    extensions.configure<KotlinMultiplatformExtension>(::configureKotlinMultiplatform)
    extensions.configure<LibraryExtension>(::configureKotlinAndroid)
  }
}
