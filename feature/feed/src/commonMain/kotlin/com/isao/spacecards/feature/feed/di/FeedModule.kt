package com.isao.spacecards.feature.feed.di

import com.isao.spacecards.core.domain.di.domainModule
import org.koin.dsl.module
import org.koin.ksp.generated.defaultModule

val feedModule = module {
  includes(defaultModule)
  includes(domainModule)
}
