import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
//    id("java-library")
//    alias(libs.plugins.jetbrains.kotlin.jvm)
  alias(libs.plugins.yfoo3.kotlinMultiplatform)
  alias(libs.plugins.sqldelight)
}

// java {
//    sourceCompatibility = JavaVersion.VERSION_11
//    targetCompatibility = JavaVersion.VERSION_11
// }

kotlin {
  // TODO uncommenting this results in Dispatchers being unavalable in module (wtf???)
//    @OptIn(ExperimentalWasmDsl::class)
//    wasmJs {
//        browser()
//    }
  listOf(
    iosX64(),
    iosArm64(),
    iosSimulatorArm64(),
  ).forEach { iosTarget ->
    iosTarget.binaries.framework {
      baseName = "ComposeApp"
      isStatic = true
    }
  }
  sourceSets {
    commonMain.dependencies {
      api(projects.core.model)
      api(projects.core.db)

      implementation(libs.koin.core)
      api(libs.koin.annotations)
      implementation("app.cash.sqldelight:coroutines-extensions:2.0.2")
    }
    androidMain.dependencies {
      implementation("app.cash.sqldelight:android-driver:2.0.2")
    }
    desktopMain.dependencies {
      implementation("app.cash.sqldelight:sqlite-driver:2.0.2")
    }
    iosMain.dependencies {
      implementation("app.cash.sqldelight:native-driver:2.0.2")
    }
    jsMain.dependencies {
      implementation(npm("sql.js", "1.6.2"))
      implementation(devNpm("copy-webpack-plugin", "9.1.0"))
//            implementation(libs.koin.core.js)
//            implementation(libs.koin.core.coroutines.js)
      implementation("app.cash.sqldelight:sqljs-driver:2.0.2")
    }
    // KSP Common sourceSet
    sourceSets.named("commonMain").configure {
      kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
    }
  }
//    compilerOptions {
//        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
//    }
}

// KSP Tasks
dependencies {
  add("kspCommonMainMetadata", libs.koin.ksp.compiler)
  add("kspAndroid", libs.koin.ksp.compiler)
  add("kspDesktop", libs.koin.ksp.compiler)
  add("kspIosX64", libs.koin.ksp.compiler)
  add("kspIosArm64", libs.koin.ksp.compiler)
  add("kspIosSimulatorArm64", libs.koin.ksp.compiler)
}

// Trigger Common Metadata Generation from Native tasks
project.tasks.withType(KotlinCompilationTask::class.java).configureEach {
  if (name != "kspCommonMainKotlinMetadata") {
    dependsOn("kspCommonMainKotlinMetadata")
  }
}

sqldelight {
  databases {
    create("Database") {
      packageName.set("com.isao.yfoo3.core.data")
//            generateAsync.set(true)
    }
  }
}
