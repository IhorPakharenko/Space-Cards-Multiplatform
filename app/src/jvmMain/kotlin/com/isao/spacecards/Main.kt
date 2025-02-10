package com.isao.spacecards

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import co.touchlab.kermit.Logger
import co.touchlab.kermit.koin.KermitKoinLogger
import com.isao.spacecards.feature.designsystem.theme.DarkColorScheme
import org.koin.core.context.startKoin

fun main() {
  startKoin {
    logger(KermitKoinLogger(Logger.withTag("koin")))
    modules(appModule)
  }
  application {
    Window(
      onCloseRequest = ::exitApplication,
      title = "Space Cards",
    ) {
      App(colorScheme = DarkColorScheme)
    }
  }
}
