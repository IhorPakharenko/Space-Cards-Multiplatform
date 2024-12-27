@file:OptIn(ExperimentalTestApi::class)

package com.isao.spacecards.presentation

import androidx.compose.material3.Text
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.isao.spacecards.utils.printSemantics
import kotlin.test.Test

class Temp {
    @Test
    fun abc() {
        runComposeUiTest {
            printSemantics()
            setContent { Text("Hi") }
            printSemantics()
//            onNodeWithContentDescription(a).assertExists()
        }
    }
}