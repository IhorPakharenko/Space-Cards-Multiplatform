package com.isao.spacecards.liked.di

import com.isao.spacecards.core.domain.di.domainModule
import com.isao.spacecards.liked.LikedViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.koin.ksp.generated.defaultModule

val likedModule = module {
  includes(defaultModule)
  includes(domainModule)
  viewModelOf(::LikedViewModel)
}
