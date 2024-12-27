@file:OptIn(ExperimentalTestApi::class)

package com.isao.spacecards.utils

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.printToString

@OptIn(ExperimentalTestApi::class)
fun ComposeUiTest.printSemantics(
    tag: String = "Semantics",
    useUnmergedTree: Boolean = false
) {
    val semantics = onAllNodes(isRoot(), useUnmergedTree).printToString(Int.MAX_VALUE)
    println("${tag}:\n${semantics}")
}