package com.isao.spacecards.feature.common.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun <T> T.delayedWhen(
  delayMillis: Duration = 300.milliseconds,
  condition: (T, T) -> Boolean,
): State<T> {
  val currentState = this
  val lastUndelayedState = remember { mutableStateOf(currentState) }
  val delayedState = remember { mutableStateOf(currentState) }

  LaunchedEffect(currentState, condition) {
    if (condition(lastUndelayedState.value, currentState)) {
      delay(delayMillis)
    } else {
      lastUndelayedState.value = currentState
    }
    delayedState.value = currentState
  }
  return delayedState
}
