package com.isao.yfoo3.feature.feed.di

import com.isao.yfoo3.core.domain.di.domainModule
import org.koin.dsl.module
import org.koin.ksp.generated.defaultModule

val feedModule = module {
  includes(defaultModule)
  includes(domainModule)
}
