package com.isao.spacecards.feature.feed.di

import com.isao.spacecards.feature.feed.FeedViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.koin.ksp.generated.defaultModule

val feedModule = module {
  includes(defaultModule)
  viewModelOf(::FeedViewModel)
}
