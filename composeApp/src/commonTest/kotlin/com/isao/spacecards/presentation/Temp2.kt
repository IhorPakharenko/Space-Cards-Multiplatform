@file:OptIn(ExperimentalTestApi::class)

package com.isao.spacecards.presentation

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.isao.spacecards.presentation.liked.LikedUiState
import com.isao.spacecards.presentation.liked.composable.LikedScreen
import com.isao.spacecards.utils.printSemantics
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