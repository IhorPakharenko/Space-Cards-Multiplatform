import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.withType

internal fun Project.configureKotlinAndroid() {
  with(pluginManager) {
    // Android Library and Android Application plugins are mutually exclusive.
    // When configuring composeApp module, apply only Application.
    if (!hasPlugin(libs.findPlugin("androidApplication").get().get().pluginId)) {
      apply(libs.findPlugin("androidLibrary").get().get().pluginId)
    }
  }
  android {
    // get module name from module path
    val moduleName = path.split(":").drop(2).joinToString(".")
    namespace =
      if (moduleName.isNotEmpty()) "com.isao.spacecards.$moduleName" else "com.isao.spacecards"

    compileSdk = libs.findVersion("android.compileSdk").get().requiredVersion.toInt()
    defaultConfig {
      minSdk = libs.findVersion("android.minSdk").get().requiredVersion.toInt()
      testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
      sourceCompatibility = JavaVersion.VERSION_17
      targetCompatibility = JavaVersion.VERSION_17
    }
    packaging {
      resources {
        excludes += "/META-INF/{AL2.0,LGPL2.1}"
      }
    }

    sourceSets.all {
      java.srcDirs("src/$name/kotlin")
    }
    @Suppress("UnstableApiUsage")
    testOptions {
      unitTests {
        isIncludeAndroidResources = true
      }
    }
    tasks.withType<Test> {
      testLogging {
        // Enables println() in unit tests
        events("standardOut")
      }
    }
  }
}
