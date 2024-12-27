package com.isao.spacecards.utils

import androidx.lifecycle.SavedStateHandle
import org.koin.dsl.module

val testsModule = module {
    factory<SavedStateHandle> { SavedStateHandle() }
}