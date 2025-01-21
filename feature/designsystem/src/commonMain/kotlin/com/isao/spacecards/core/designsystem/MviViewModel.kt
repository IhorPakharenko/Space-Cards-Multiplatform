package com.isao.spacecards.core.designsystem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
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

@OptIn(ExperimentalCoroutinesApi::class)
abstract class MviViewModel<UI_STATE, PARTIAL_UI_STATE, EVENT, INTENT>(initialState: UI_STATE) :
  ViewModel() {
  private val intentFlow = MutableSharedFlow<INTENT>()
  private val continuousPartialStateFlow =
    MutableStateFlow<List<Flow<PARTIAL_UI_STATE>>>(emptyList())

  private val intentFlowListenerStarted = CompletableDeferred<Unit>()
  private val continuousPartialStateFlowListenerStarted = CompletableDeferred<Unit>()

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
  ).onEach { Logger.d("New partial state:\n$it") }
    .scan(initialState, ::reduceUiState)
    .onEach { privateUiStateSnapshot.value = it }
    .onEach { Logger.d("New state:\n$it") }
    //TODO don't catch everything.
    // Would removing this cause ViewModel to break even if an exception would be handled later elsewhere?
    // Instead of catching everything, this might be a good place to catch errors like
    // no more memory available, no internet, 500, 400, invalid json
//    .catch { Logger.e(it.message.toString(), it) }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5.seconds), initialState)

  private val eventChannel = Channel<EVENT>(Channel.BUFFERED)
  val event = eventChannel.receiveAsFlow()

  private fun userIntents(): Flow<PARTIAL_UI_STATE> = intentFlow
    .onStart { intentFlowListenerStarted.complete(Unit) }
    .flatMapConcat(::mapIntents)

  private fun continuousFlows(): Flow<List<Flow<PARTIAL_UI_STATE>>> = continuousPartialStateFlow
    .onStart { continuousPartialStateFlowListenerStarted.complete(Unit) }

  fun acceptIntent(intent: INTENT) {
    viewModelScope.launch {
      intentFlowListenerStarted.await()
      intentFlow.emit(intent)
    }
  }

  protected fun observeContinuousChanges(changesFlow: Flow<PARTIAL_UI_STATE>) {
    viewModelScope.launch {
      continuousPartialStateFlowListenerStarted.await()
      continuousPartialStateFlow.update { it + changesFlow }
    }
  }

  protected fun publishEvent(event: EVENT) {
    viewModelScope.launch {
      eventChannel.send(event)
    }
  }

  protected abstract fun mapIntents(intent: INTENT): Flow<PARTIAL_UI_STATE>

  protected abstract fun reduceUiState(
    previousState: UI_STATE,
    partialState: PARTIAL_UI_STATE,
  ): UI_STATE

  protected fun <UI_STATE_PART> observeContinuousChanges(
    dependingOnState: (UI_STATE) -> UI_STATE_PART,
    getChangesFlow: (UI_STATE_PART) -> Flow<PARTIAL_UI_STATE>,
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
}
