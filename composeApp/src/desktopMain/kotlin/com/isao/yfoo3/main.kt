package com.isao.yfoo3

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import co.touchlab.kermit.Logger
import co.touchlab.kermit.koin.KermitKoinLogger
import com.isao.yfoo3.core.di.appModule
import org.koin.core.context.startKoin

fun main() {
    startKoin {
        logger(KermitKoinLogger(Logger.withTag("koin")))
        modules(appModule)
    }
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Yfoo Multiplatform",
        ) {
            App()
        }
    }
}