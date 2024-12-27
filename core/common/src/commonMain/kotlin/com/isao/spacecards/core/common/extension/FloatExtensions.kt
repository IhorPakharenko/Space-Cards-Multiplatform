package com.isao.spacecards.core.common.extension

/**
 * Scales float value from an old range to a new one
 */
fun Float.scale(
  oldMin: Float,
  oldMax: Float,
  newMin: Float,
  newMax: Float,
): Float {
  val oldRange = oldMax - oldMin
  val newRange = newMax - newMin

  if (oldRange == 0f) return newMin

  return (((this - oldMin) * newRange) / oldRange) + newMin
}
