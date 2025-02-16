package com.isao.spacecards.feature.common.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * When condition is true, the state will be delayed for [delay] before being emitted.
 * When condition is false, the state will be emitted immediately.
 *
 * This is useful for displaying a non-loading state when the actual state is loading,
 * but it might load so fast the UI might flicker if the loading state is displayed right away.
 */
@Composable
fun <T> T.delayedWhen(
  delay: Duration = 300.milliseconds,
  condition: (T, T) -> Boolean,
): State<T> {
  val currentState = this
  val lastUndelayedState = remember { mutableStateOf(currentState) }
  val delayedState = remember { mutableStateOf(currentState) }

  LaunchedEffect(currentState, condition) {
    if (condition(lastUndelayedState.value, currentState)) {
      delay(delay)
    } else {
      lastUndelayedState.value = currentState
    }
    delayedState.value = currentState
  }
  return delayedState
}
