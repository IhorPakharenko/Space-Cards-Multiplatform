package com.isao.yfoo3.liked.di

import com.isao.yfoo3.core.domain.di.domainModule
import org.koin.dsl.module
import org.koin.ksp.generated.defaultModule

val likedModule = module {
    includes(defaultModule)
    includes(domainModule)
}