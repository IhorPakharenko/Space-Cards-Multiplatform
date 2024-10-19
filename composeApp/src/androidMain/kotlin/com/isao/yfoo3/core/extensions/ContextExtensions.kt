package com.isao.yfoo3.core.extensions

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

/**
 * Find the closest Activity in a given Context.
 * Idea taken from Accompanist:
 * https://github.com/google/accompanist/blob/6611ebda55eb2948eca9e1c89c2519e80300855a/permissions/src/main/java/com/google/accompanist/permissions/PermissionsUtil.kt#L99
 */
internal fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}