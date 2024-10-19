package com.isao.yfoo3

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Yfoo Multiplatform",
    ) {
        App()
    }
}