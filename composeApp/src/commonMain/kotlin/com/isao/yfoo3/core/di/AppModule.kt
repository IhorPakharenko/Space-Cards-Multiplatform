package com.isao.yfoo3.core.di

import com.isao.yfoo3.data.di.databaseModule
import org.koin.dsl.module
import org.koin.ksp.generated.defaultModule

val appModule = module {
    includes(databaseModule, defaultModule)
}