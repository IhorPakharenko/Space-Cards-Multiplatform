import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureComposeAndroid() {
  android {
    buildFeatures {
      compose = true
    }

    @Suppress("UnstableApiUsage")
    experimentalProperties["android.experimental.enableScreenshotTest"] = true

    dependencies {
      add("implementation", libs.findLibrary("androidx-ui-tooling-preview-android").get())
      add("debugImplementation", libs.findLibrary("compose-ui-tooling").get())
    }
  }
}
