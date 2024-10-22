package com.isao.yfoo3.utils

import androidx.lifecycle.SavedStateHandle
import org.koin.dsl.module

val testsModule = module {
    factory<SavedStateHandle> { SavedStateHandle() }
}