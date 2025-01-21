import com.github.gmazzo.buildconfig.BuildConfigExtension
import com.github.gmazzo.buildconfig.BuildConfigField
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.Project
import org.gradle.kotlin.dsl.buildConfigField
import java.util.Properties

class Secrets(
  private val project: Project,
  private val buildConfigExtension: BuildConfigExtension,
) {
  enum class Key {
    ASTROBIN_API_KEY,
    ASTROBIN_API_SECRET,
  }

  private val secrets: Properties by lazy {
    val properties = Properties()
    val secretsFile = project.rootProject.file("secrets.properties")
    if (secretsFile.exists()) {
      secretsFile.inputStream().use { properties.load(it) }
    }
    properties
  }

  fun get(
    key: String,
    fallback: String? = null,
  ): String = secrets.getProperty(key) ?: System.getenv(key) ?: fallback
    ?: throw IllegalArgumentException(
      "Secret not found: $key. Please add it to secrets.properties or set an environment variable.",
    )

  fun buildConfigField(
    key: Key,
    fallback: String? = null,
  ): NamedDomainObjectProvider<BuildConfigField> =
    buildConfigExtension.buildConfigField(key.name, get(key.name, fallback))
}
