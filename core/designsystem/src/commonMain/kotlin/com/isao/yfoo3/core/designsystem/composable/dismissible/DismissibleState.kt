package com.isao.yfoo3.core.designsystem.composable.dismissible

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.Velocity
import com.isao.yfoo3.core.common.util.toRadians
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.hypot
import kotlin.math.sin

enum class DismissDirection {
  Start,
  End,
  Up,
  Down,
}

@Composable
fun rememberDismissibleState(
  onDismiss: DismissibleState.(DismissDirection) -> Unit = {},
): DismissibleState {
  val layoutDirection = LocalLayoutDirection.current
  val onDismissState = rememberUpdatedState(onDismiss)
  return remember {
    DismissibleState(
      layoutDirection,
    ) { onDismissState.value.invoke(this, it) }
  }
}

@Stable
class DismissibleState(
  private val layoutDirection: LayoutDirection,
  val onDismiss: DismissibleState.(DismissDirection) -> Unit,
) {
  private val offset = Animatable(Offset.Zero, Offset.VectorConverter)

  internal var directions: Set<DismissDirection> by mutableStateOf(emptySet())
  internal var containerWidth: Float by mutableFloatStateOf(0f)
  internal var containerHeight: Float by mutableFloatStateOf(0f)
  internal var maxRotationZ: Float by mutableFloatStateOf(0f)
  internal var velocityThreshold: Float by mutableFloatStateOf(0f)
  internal var minHorizontalProgressThreshold: Float by mutableFloatStateOf(0f)
  internal var minVerticalProgressThreshold: Float by mutableFloatStateOf(0f)

  val value get() = offset.value
  val targetValue get() = offset.targetValue

  /**
   * The fraction of the progress going from the middle of the available space
   * to the edge in horizontal plane.
   * The value is equal to 0f when the card is in its initial position.
   * The value is roughly within -1f..1f bounds, but will go beyond these bounds
   * during dismiss to be fully hidden when the card is rotated.
   *
   * Not coerced by allowed directions.
   */
  val horizontalDismissProgress by derivedStateOf {
    offset.value.x / containerWidth * if (layoutDirection == LayoutDirection.Rtl) -1 else 1
  }

  /**
   * The fraction of the progress going from the middle of the available space
   * to the edge in vertical plane.
   * The value is equal to 0f when the card is in its initial position.
   * The value is roughly within -1f..1f bounds, but will go beyond these bounds
   * during dismiss to be fully hidden when the card is rotated.
   *
   * Not coerced by allowed directions.
   */
  val verticalDismissProgress by derivedStateOf {
    offset.value.y / containerHeight
  }

  /**
   * The fraction of the progress going from the middle of the available space
   * to the edge in both horizontal and vertical planes.
   * The value is roughly within 0..1f bounds, but will go beyond these bounds
   * during dismiss to be fully hidden when the card is rotated.
   *
   * Not coerced by allowed directions.
   */
  val combinedDismissProgress by derivedStateOf {
    hypot(horizontalDismissProgress.absoluteValue, verticalDismissProgress.absoluteValue)
  }

  val rotationZ by derivedStateOf {
    maxRotationZ * offset.value.x / containerWidth
  }

  /**
   * The [DismissDirection] (if any) in which the composable has been or is being dismissed.
   */
  var dismissDirection: DismissDirection? by mutableStateOf(null)
    private set

  private val endX by derivedStateOf {
    getEndX(containerWidth = containerWidth, containerHeight = containerHeight).toFloat()
  }
  private val endY by derivedStateOf {
    getEndY(containerWidth = containerWidth, containerHeight = containerHeight).toFloat()
  }

  suspend fun reset(
    animationSpec: AnimationSpec<Offset>? = spring(
      dampingRatio = Spring.DampingRatioMediumBouncy,
      stiffness = Spring.StiffnessLow,
    ),
  ) {
    dismissDirection = null
    if (animationSpec != null) {
      offset.animateTo(Offset.Zero, animationSpec)
    } else {
      offset.snapTo(Offset.Zero)
    }
  }

  suspend fun dismiss(
    direction: DismissDirection,
    spec: AnimationSpec<Offset> = tween(1000),
  ) {
    dismissDirection = direction
    val directionMultiplier = if (layoutDirection == LayoutDirection.Rtl) -1 else 1
    when (direction) {
      DismissDirection.Start -> offset.animateTo(
        offset(x = -endX * directionMultiplier),
        spec,
      )

      DismissDirection.End -> offset.animateTo(offset(x = endX * directionMultiplier), spec)
      DismissDirection.Up -> offset.animateTo(offset(y = -endY), spec)
      DismissDirection.Down -> offset.animateTo(offset(y = endY), spec)
    }
    onDismiss(direction)
  }

  internal suspend fun performFling(velocity: Velocity) {
    val directionMultiplier =
      if (layoutDirection == LayoutDirection.Rtl) -1 else 1

    val coercedVelocity = velocity.coerceIn(
      allowedDirections = directions,
    )
    val dismissDirectionDueToVelocity = getDismissDirection(
      valueX = coercedVelocity.x * directionMultiplier,
      valueY = coercedVelocity.y,
      minValueX = velocityThreshold,
      minValueY = velocityThreshold,
    )

    val coercedOffset = offset.targetValue.coerceIn(
      allowedDirections = directions,
      maxWidth = containerWidth,
      maxHeight = containerHeight,
    )
    val dismissDirectionDueToOffset = getDismissDirection(
      valueX = coercedOffset.x * directionMultiplier,
      valueY = coercedOffset.y,
      minValueX = containerWidth * minHorizontalProgressThreshold,
      minValueY = containerHeight * minVerticalProgressThreshold,
    )

    val dismissDirection: DismissDirection? =
      dismissDirectionDueToVelocity ?: dismissDirectionDueToOffset
    if (dismissDirection != null) {
      dismiss(dismissDirection, tween(500, easing = LinearOutSlowInEasing))
    } else {
      reset()
    }
  }

  internal suspend fun performDrag(dragged: Offset) {
    val original = offset.targetValue
    val summed = original + dragged
    offset.animateTo(
      offset(
        x = summed.x.coerceIn(-containerWidth, containerWidth),
        y = summed.y.coerceIn(-containerHeight, containerHeight),
      ),
    )
  }

  private fun offset(
    x: Float = offset.value.x,
    y: Float = offset.value.y,
  ): Offset = Offset(x, y)

  private fun getEndX(
    containerWidth: Float,
    containerHeight: Float,
  ): Double {
    val maxRotationsRadians = toRadians(maxRotationZ.toDouble())
    val ninetyDegreesRadians = toRadians(90.0)
    return abs(containerWidth * sin(ninetyDegreesRadians - maxRotationsRadians)) +
      abs(containerHeight * sin(maxRotationsRadians))
  }

  private fun getEndY(
    containerWidth: Float,
    containerHeight: Float,
  ): Double {
    val maxRotationsRadians = toRadians(maxRotationZ.toDouble())
    val ninetyDegreesRadians = toRadians(90.0)
    return abs(containerHeight * sin(ninetyDegreesRadians - maxRotationsRadians)) +
      abs(containerWidth * sin(maxRotationsRadians))
  }

  private fun Offset.coerceIn(
    allowedDirections: Set<DismissDirection>,
    maxWidth: Float,
    maxHeight: Float,
  ): Offset = copy(
    x = x.coerceWidthIn(allowedDirections, maxWidth),
    y = y.coerceHeightIn(allowedDirections, maxHeight),
  )

  private fun Velocity.coerceIn(allowedDirections: Set<DismissDirection>): Velocity = copy(
    x = x.coerceWidthIn(allowedDirections, Float.MAX_VALUE),
    y = y.coerceHeightIn(allowedDirections, Float.MAX_VALUE),
  )

  private fun Float.coerceWidthIn(
    allowedDirections: Set<DismissDirection>,
    maxWidth: Float,
  ): Float = coerceIn(
    if (allowedDirections.contains(DismissDirection.Start)) -maxWidth else 0f,
    if (allowedDirections.contains(DismissDirection.End)) maxWidth else 0f,
  )

  private fun Float.coerceHeightIn(
    allowedDirections: Set<DismissDirection>,
    maxHeight: Float,
  ): Float = coerceIn(
    if (allowedDirections.contains(DismissDirection.Up)) -maxHeight else 0f,
    if (allowedDirections.contains(DismissDirection.Down)) maxHeight else 0f,
  )

  /**
   * Finds the direction depending on which value is closest to its corresponding minValue
   * @return The direction of the dismiss action
   * or null if not the conditions for the dismiss have not been met
   */
  private fun getDismissDirection(
    valueX: Float,
    valueY: Float,
    minValueX: Float,
    minValueY: Float,
  ): DismissDirection? = listOf(
    DismissDirection.End to valueX / minValueX,
    DismissDirection.Start to valueX.absoluteValue / minValueX,
    DismissDirection.Down to valueY / minValueY,
    DismissDirection.Up to valueY.absoluteValue / minValueY,
  ).sortedBy { (_, ratio) -> ratio }
    .firstOrNull { (_, ratio) ->
      ratio >= 1
    }?.first
}
