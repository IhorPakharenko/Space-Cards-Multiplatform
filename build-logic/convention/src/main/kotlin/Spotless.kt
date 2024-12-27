// Copyright 2023, Christopher Banes and the Tivi project contributors
// SPDX-License-Identifier: Apache-2.0

import com.diffplug.gradle.spotless.SpotlessExtension
import com.isao.yfoo3.libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

fun Project.configureSpotless() {
  with(pluginManager) {
    apply("com.diffplug.spotless")
  }

  spotless {
    ratchetFrom = (project.properties["ratchetFrom"] as? String)
      ?.trim()
      ?.takeIf { it.isNotEmpty() }

    val ktlintVersion = libs.findVersion("ktlint").get().toString()
    val ktlintComposeVersion = libs.findVersion("ktlintCompose").get().toString()

    kotlin {
      target("src/**/*.kt")
      ktlint(ktlintVersion)
        .customRuleSets(
          listOf(
            "io.nlopez.compose.rules:ktlint:$ktlintComposeVersion",
          ),
        )
      // Suppressing non-autofixable lints causes spotless to silently skip files with said lints.
    }
    kotlinGradle {
      target("**/*.kts")
      targetExclude("**/build/**/*.kts")
      ktlint(ktlintVersion)
    }
    format("xml") {
      target("**/*.xml")
      targetExclude("**/build/**/*.xml")
    }
  }
}

private fun Project.spotless(action: SpotlessExtension.() -> Unit) =
  extensions.configure<SpotlessExtension>(action)
// To run for all files:
// ./gradlew --no-daemon --continue spotlessApply
// To run for a specific file:
// ./gradlew spotlessApply -PspotlessIdeHook={ABSOLUTE_PATH_TO_FILE}
// Until this issue (https://github.com/diffplug/spotless/issues/1101) is resolved,
// Even using the IDE hook is too slow in multi-module projects to replace autoformatting.
// There are other problems which are present in both the gradle plugin
// and the current IDE plugin (which uses the gradle plugin under the hood)
// https://github.com/diffplug/spotless/issues/1924
// https://github.com/diffplug/spotless/issues/2365
