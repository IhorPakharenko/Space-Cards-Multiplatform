package com.isao.yfoo3.core.utils

import org.jetbrains.compose.ui.tooling.preview.Preview

//TODO ui-tooling-preview-android is unavailable for other platforms
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.FUNCTION
)
@Preview
annotation class PreviewLightDark