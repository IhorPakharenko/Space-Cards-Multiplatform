package com.isao.spacecards.utils

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher

fun hasNoText(): SemanticsMatcher {
    val propertyName = "${SemanticsProperties.Text.name} + ${SemanticsProperties.EditableText.name}"
    return SemanticsMatcher("$propertyName contains no text") {
        val editableText = it.config.getOrNull(SemanticsProperties.EditableText)
        val text = it.config.getOrNull(SemanticsProperties.Text)
        editableText == null && text == null
    }
}