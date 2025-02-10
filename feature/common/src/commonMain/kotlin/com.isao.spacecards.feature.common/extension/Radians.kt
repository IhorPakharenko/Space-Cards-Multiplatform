package com.isao.spacecards.feature.common.extension

import kotlin.math.PI

fun Double.degreesToRadians(): Double = this / 180.0 * PI

fun Double.radiansToDegrees(): Double = this * 180.0 / PI
