package com.isao.yfoo3.core.designsystem.composable.dismissible

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.changedToDown
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.ObserverModifierNode
import androidx.compose.ui.node.PointerInputModifierNode
import androidx.compose.ui.node.currentValueOf
import androidx.compose.ui.node.observeReads
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


fun Modifier.dismissible(
    state: DismissibleState,
    directions: Set<DismissDirection>,
    enabled: Boolean = true,
    maxRotationZ: Float = 15f,
    dismissThresholds: DismissThresholds = DismissThresholds(),
) = this then DismissibleElement(
    state,
    directions,
    enabled,
    maxRotationZ,
    dismissThresholds,
)

data class DismissThresholds(
    val velocityThreshold: Dp = 500.dp,
    val minHorizontalProgressThreshold: Float = 0.7f,
    val minVerticalProgressThreshold: Float = 0.7f,
)

private data class DismissibleElement(
    val state: DismissibleState,
    val directions: Set<DismissDirection>,
    val enabled: Boolean,
    val maxRotationZ: Float,
    val dismissThresholds: DismissThresholds,
) : ModifierNodeElement<DismissibleNode>() {
    override fun create(): DismissibleNode {
        state.directions = directions
        state.maxRotationZ = maxRotationZ
        state.minHorizontalProgressThreshold = dismissThresholds.minHorizontalProgressThreshold
        state.minVerticalProgressThreshold = dismissThresholds.minVerticalProgressThreshold

        return DismissibleNode(
            state,
            directions,
            enabled,
            maxRotationZ,
            dismissThresholds,
        )
    }

    override fun update(node: DismissibleNode) {
        node.state = state
        node.directions = directions
        node.enabled = enabled
        node.maxRotationZ = maxRotationZ
        node.dismissThresholds = dismissThresholds

        state.directions = directions
        state.maxRotationZ = maxRotationZ
        state.minHorizontalProgressThreshold = dismissThresholds.minHorizontalProgressThreshold
        state.minVerticalProgressThreshold = dismissThresholds.minVerticalProgressThreshold
    }

}

private class DismissibleNode(
    var state: DismissibleState,
    var directions: Set<DismissDirection>,
    var enabled: Boolean,
    var maxRotationZ: Float,
    var dismissThresholds: DismissThresholds,
) : Modifier.Node(),
    DrawModifierNode,
    PointerInputModifierNode,
    LayoutModifierNode,
    ObserverModifierNode,
    CompositionLocalConsumerModifierNode {

    private val velocityTracker = VelocityTracker()

    override fun onAttach() {
        super.onAttach()

        with(dismissThresholds) {
            check(minHorizontalProgressThreshold > 0f && minHorizontalProgressThreshold <= 1) {
                "minHorizontalProgressToDismiss must be greater than 0 and less than or equal to 1"
            }
            check(minVerticalProgressThreshold > 0f && minVerticalProgressThreshold <= 1) {
                "minVerticalProgressToDismiss must be greater than 0 and less than or equal to 1"
            }
        }

        onObservedReadsChanged()
    }

    // observeReads does not work for LocalDensity yet:
    // https://issuetracker.google.com/issues/318434914
    override fun onObservedReadsChanged() {
        observeReads {
            with(currentValueOf(LocalDensity)) {
                state.velocityThreshold = dismissThresholds.velocityThreshold.toPx()
            }
        }
    }

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        val placeable = measurable.measure(constraints)

        state.containerWidth = placeable.width.toFloat()
        state.containerHeight = placeable.height.toFloat()

        return layout(placeable.width, placeable.height) {
            placeable.place(0, 0)
        }
    }

    override fun ContentDrawScope.draw() {
        translate(left = state.value.x, top = state.value.y) {
            rotate(state.rotationZ) {
                this@draw.drawContent()
            }
        }
    }

    override fun onCancelPointerInput() {
        runCatching {
            coroutineScope.launch {
                state.reset()
            }
        }
    }

    override fun onPointerEvent(
        pointerEvent: PointerEvent,
        pass: PointerEventPass,
        bounds: IntSize
    ) {
        if (!enabled) return
        if (state.dismissDirection != null) return

        when (pass) {
            PointerEventPass.Initial -> {
                // Handle drag start
                for (change in pointerEvent.changes) {
                    if (change.changedToDown()) {
                        velocityTracker.resetTracking()
                        change.consume()
                    }
                }
            }

            PointerEventPass.Main -> {
                // Handle drag events
                for (change in pointerEvent.changes) {
                    if (change.pressed) {
                        val dragAmount = change.positionChange()
                        change.consume()
                        velocityTracker.addPosition(change.uptimeMillis, change.position)
                        coroutineScope.launch {
                            state.performDrag(dragAmount)
                        }
                    }
                }
            }

            PointerEventPass.Final -> {
                // Handle drag end
                for (change in pointerEvent.changes) {
                    if (change.changedToUp()) {
                        coroutineScope.launch {
                            state.performFling(velocityTracker.calculateVelocity())
                        }
                        change.consume()
                    }
                }
            }
        }
    }
}
