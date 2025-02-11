package com.isao.spacecards.feature.designsystem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

typealias Reducer<UI_STATE> = UI_STATE.() -> UI_STATE

@OptIn(ExperimentalCoroutinesApi::class)
abstract class MviViewModel<UI_STATE, EVENT, INTENT>(initialState: UI_STATE) : ViewModel() {
  private val intentFlow = MutableSharedFlow<INTENT>()
  private val continuousReducerFlow = MutableStateFlow<List<Flow<Reducer<UI_STATE>>>>(emptyList())

  private val intentFlowListenerStarted = CompletableDeferred<Unit>()
  private val continuousReducerFlowListenerStarted = CompletableDeferred<Unit>()

  private val privateUiStateSnapshot = MutableStateFlow(initialState)

  /**
   * The flow of UI state which can safely be observed forever.
   * It only contains a copy of the latest UI state.
   * Observing this will NOT cause state updates.
   */
  protected val uiStateSnapshot = privateUiStateSnapshot.asStateFlow()

  /**
   * The flow of UI state which should be accessed only from the UI.
   * Observing this will cause state updates.
   */
  val uiState = merge(
    userIntents(),
    continuousFlows().flatMapConcat { it.merge() },
  ).scan(initialState) { state, reducer -> reducer(state) }
    .onEach { privateUiStateSnapshot.value = it }
    // The last line of defense against unhandled exceptions (except Thread.UncaughtExceptionHandler).
    // Most should be allowed to crash the app as we don't know how to recover.
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5.seconds), initialState)

  private val eventChannel = Channel<EVENT>(Channel.BUFFERED)
  val event = eventChannel.receiveAsFlow()

  private fun userIntents(): Flow<Reducer<UI_STATE>> = intentFlow
    .onStart { intentFlowListenerStarted.complete(Unit) }
    .flatMapConcat(::mapIntents)

  private fun continuousFlows(): Flow<List<Flow<Reducer<UI_STATE>>>> = continuousReducerFlow
    .onStart { continuousReducerFlowListenerStarted.complete(Unit) }

  fun acceptIntent(intent: INTENT) {
    viewModelScope.launch {
      intentFlowListenerStarted.await()
      intentFlow.emit(intent)
    }
  }

  /**
   * Does not trigger state update right away. Instead, it returns a new reducer
   * which will be applied to the current state when the state is observed.
   */
  protected fun updateState(reduce: Reducer<UI_STATE>): Reducer<UI_STATE> = reduce

  /**
   * Does not trigger state update right away. Instead, it returns a flow of a new reducer
   * which will be applied to the current state when the state is observed.
   */
  protected fun flowOfUpdateState(reduce: Reducer<UI_STATE>): Flow<Reducer<UI_STATE>> =
    flowOf(reduce)

  protected fun publishEvent(event: EVENT) {
    viewModelScope.launch {
      eventChannel.send(event)
    }
  }

  protected fun observeContinuousChanges(changesFlow: Flow<Reducer<UI_STATE>>) {
    viewModelScope.launch {
      continuousReducerFlowListenerStarted.await()
      continuousReducerFlow.update { it + changesFlow }
    }
  }

  protected fun <UI_STATE_PART> observeContinuousChanges(
    dependingOnState: (UI_STATE) -> UI_STATE_PART,
    getChangesFlow: (UI_STATE_PART) -> Flow<Reducer<UI_STATE>>,
  ) {
    observeContinuousChanges(
      uiStateSnapshot
        .map(dependingOnState)
        .distinctUntilChanged()
        .flatMapLatest { dependency ->
          getChangesFlow(dependency)
        },
    )
  }

  protected abstract fun mapIntents(intent: INTENT): Flow<Reducer<UI_STATE>>
}
