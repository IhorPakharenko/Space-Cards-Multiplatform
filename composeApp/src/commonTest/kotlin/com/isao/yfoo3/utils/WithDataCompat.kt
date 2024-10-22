package com.isao.yfoo3.utils

import io.kotest.core.spec.style.FunSpec
import io.kotest.core.spec.style.scopes.BehaviorSpecGivenContainerScope
import io.kotest.core.spec.style.scopes.BehaviorSpecRootScope
import io.kotest.core.spec.style.scopes.BehaviorSpecWhenContainerScope
import io.kotest.core.test.TestScope

/**
 * Normal data-driven tests in Kotest don't work too well in Android Studio.
 * This function mimics the functionality of [io.kotest.datatest.withData].
 */
suspend fun <T> BehaviorSpecRootScope.GivenWithData(
    name: (T) -> String,
    data: List<T>,
    test: suspend BehaviorSpecGivenContainerScope.(T) -> Unit
) {
    data.forEach { element ->
        Given(name(element)) {
            test(element)
        }
    }
}

/**
 * Normal data-driven tests in Kotest don't work too well on Android Studio.
 * This function mimics the functionality of [io.kotest.datatest.withData].
 */
suspend fun <T> BehaviorSpecGivenContainerScope.WhenWithData(
    name: (T) -> String,
    data: List<T>,
    test: suspend BehaviorSpecWhenContainerScope.(T) -> Unit
) {
    data.forEach { element ->
        When(name(element)) {
            test(element)
        }
    }
}

/**
 * Normal data-driven tests in Kotest don't work too well on Android Studio.
 * This function mimics the functionality of [io.kotest.datatest.withData].
 */
suspend fun <T> BehaviorSpecGivenContainerScope.ThenWithData(
    name: (T) -> String,
    data: List<T>,
    test: suspend TestScope.(T) -> Unit
) {
    data.forEach { element ->
        Then(name(element)) {
            test(element)
        }
    }
}

/**
 * Normal data-driven tests in Kotest don't work too well on Android Studio.
 * This function mimics the functionality of [io.kotest.datatest.withData].
 */
suspend fun <T> BehaviorSpecWhenContainerScope.ThenWithData(
    name: (T) -> String,
    data: List<T>,
    test: suspend TestScope.(T) -> Unit
) {
    data.forEach { element ->
        Then(name(element)) {
            test(element)
        }
    }
}

/**
 * Normal data-driven tests in Kotest don't work too well on Android Studio.
 * This function mimics the functionality of [io.kotest.datatest.withData].
 */
suspend fun <T> BehaviorSpecRootScope.xGivenWithData(
    name: (T) -> String,
    data: List<T>,
    test: suspend BehaviorSpecGivenContainerScope.(T) -> Unit
) {
    data.forEach { element ->
        xGiven(name(element)) {
            test(element)
        }
    }
}

/**
 * Normal data-driven tests in Kotest don't work too well on Android Studio.
 * This function mimics the functionality of [io.kotest.datatest.withData].
 */
suspend fun <T> BehaviorSpecGivenContainerScope.xWhenWithData(
    name: (T) -> String,
    data: List<T>,
    test: suspend BehaviorSpecWhenContainerScope.(T) -> Unit
) {
    data.forEach { element ->
        xWhen(name(element)) {
            test(element)
        }
    }
}

/**
 * Normal data-driven tests in Kotest don't work too well on Android Studio.
 * This function mimics the functionality of [io.kotest.datatest.withData].
 */
suspend fun <T> BehaviorSpecGivenContainerScope.xThenWithData(
    name: (T) -> String,
    data: List<T>,
    test: suspend TestScope.(T) -> Unit
) {
    data.forEach { element ->
        xThen(name(element)) {
            test(element)
        }
    }
}

/**
 * Normal data-driven tests in Kotest don't work too well on Android Studio.
 * This function mimics the functionality of [io.kotest.datatest.withData].
 */
suspend fun <T> BehaviorSpecWhenContainerScope.xThenWithData(
    name: (T) -> String,
    data: List<T>,
    test: suspend TestScope.(T) -> Unit
) {
    data.forEach { element ->
        xThen(name(element)) {
            test(element)
        }
    }
}

fun <T> FunSpec.contextWithData(
    name: (T) -> String,
    data: List<T>,
    test: suspend FunSpec.(T) -> Unit
) {
    data.forEach { element ->
        context(name(element)) {
            test(element)
        }
    }
}

fun <T> FunSpec.testWithData(
    name: (T) -> String,
    data: List<T>,
    test: suspend FunSpec.(T) -> Unit
) {
    data.forEach { element ->
        test(name(element)) {
            test(element)
        }
    }
}

fun <T> FunSpec.xcontextWithData(
    name: (T) -> String,
    data: List<T>,
    test: suspend FunSpec.(T) -> Unit
) {
    data.forEach { element ->
        xcontext(name(element)) {
            test(element)
        }
    }
}

fun <T> FunSpec.xtestWithData(
    name: (T) -> String,
    data: List<T>,
    test: suspend FunSpec.(T) -> Unit
) {
    data.forEach { element ->
        xtest(name(element)) {
            test(element)
        }
    }
}