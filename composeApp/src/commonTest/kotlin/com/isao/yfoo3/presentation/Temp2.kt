@file:OptIn(ExperimentalTestApi::class)

package com.isao.yfoo3.presentation

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.isao.yfoo3.presentation.liked.LikedUiState
import com.isao.yfoo3.presentation.liked.composable.LikedScreen
import com.isao.yfoo3.utils.printSemantics
import io.kotest.core.spec.style.FunSpec

class Temp2 : FunSpec() {
    init {
        test("abc") {
            runComposeUiTest {
                printSemantics()
                setContent { LikedScreen(LikedUiState(), {}) }
                printSemantics()
//            onNodeWithContentDescription(a).assertExists()
            }
        }
    }
}