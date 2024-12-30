import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal val Project.libs
  get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun Project.kotlinMultiplatform(action: KotlinMultiplatformExtension.() -> Unit) =
  extensions.configure<KotlinMultiplatformExtension>(action)

internal fun Project.composeCompiler(block: ComposeCompilerGradlePluginExtension.() -> Unit) {
  extensions.configure<ComposeCompilerGradlePluginExtension>(block)
}

internal fun Project.ksp(block: KspExtension.() -> Unit) {
  extensions.configure<KspExtension>(block)
}

// CommonExtension is a parent of Android Library and Android Application plugins.
internal fun Project.android(action: CommonExtension<*, *, *, *, *, *>.() -> Unit) {
  val commonExtension = extensions.findByType<ApplicationExtension>()
    ?: extensions.getByType<LibraryExtension>()
  commonExtension.apply(action)
}
