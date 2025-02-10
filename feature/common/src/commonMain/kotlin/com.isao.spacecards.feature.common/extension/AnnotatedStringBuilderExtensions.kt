package com.isao.spacecards.feature.common.extension

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun AnnotatedString.Builder.appendStringResourceWithStyles(
  resource: StringResource,
  vararg formatArgsToStyles: Pair<Any, SpanStyle>,
) {
  val formattedString =
    stringResource(resource, *formatArgsToStyles.map { it.first }.toTypedArray())
  append(formattedString)
  formatArgsToStyles.forEach { (formatArg, style) ->
    val styleStart = formattedString.indexOf(formatArg.toString())
    val styleEnd = styleStart + formatArg.toString().length
    addStyle(style, styleStart, styleEnd)
  }
}
