@file:OptIn(ExperimentalCoroutinesApi::class)

package com.isao.yfoo3.utils

import app.cash.turbine.Event
import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import io.kotest.core.coroutines.backgroundScope
import io.kotest.core.test.TestScope
import io.kotest.core.test.testCoroutineScheduler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher

/**
 * Runs all scheduled operations and returns the latest flow value.
 * Should produce the same results as [getLatestState], unless there are undiscovered differences.
 */
suspend fun <T> StateFlow<T>.testValue(): T {
    test { cancelAndIgnoreRemainingEvents() }
    return value
}

/**
 * Runs all scheduled operations and returns the latest flow value.
 * Should produce the same results as [testValue], unless there are undiscovered differences.
 */
@OptIn(ExperimentalStdlibApi::class)
fun <T> TestScope.getLatestState(flow: StateFlow<T>): T {
    backgroundScope.launch(UnconfinedTestDispatcher(testCoroutineScheduler)) {
        flow.collect()
    }
    return flow.value
}

/**
 * Awaits for an item in the flow that matches the given [condition].
 * Discards all other items. If the flow completes before finding the item,
 * throws an AssertionError.
 *
 * @param condition The condition to match against the items in the flow.
 * @return The first item that matches the condition.
 * @throws AssertionError if the flow completes without finding a matching item.
 */
suspend fun <T> ReceiveTurbine<T>.awaitItemMatching(condition: (T) -> Boolean): T {
    while (true) {
        when (val event = awaitEvent()) {
            is Event.Item -> {
                if (condition(event.value)) {
                    return event.value
                }
            }

            is Event.Complete -> {
                throw AssertionError("Flow completed without finding a matching item.")
            }

            is Event.Error -> {
                throw event.throwable
            }
        }
    }
}

/**
 * Cancels the subscription and consumes all remaining events.
 * Returns a list of all non-final states (items) encountered.
 * Throws an error if any of the events is an error.
 *
 * @return A list of all non-final states (items).
 * @throws Throwable if any of the events is an error.
 */
suspend fun <T> ReceiveTurbine<T>.consumeNonFinalStates(): List<T> {
    val states = mutableListOf<T>()

    // Cancel the turbine and consume all remaining events
    val events = cancelAndConsumeRemainingEvents()
    for (event in events) {
        when (event) {
            is Event.Item -> states.add(event.value)
            is Event.Error -> throw event.throwable
            // We don't need to handle ReceiveTurbine.Event.Complete as it's final
            Event.Complete -> {}
        }
    }

    return states
}