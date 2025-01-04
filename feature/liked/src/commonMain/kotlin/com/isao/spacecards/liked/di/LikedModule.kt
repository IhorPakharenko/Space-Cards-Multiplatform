package com.isao.spacecards.liked.di

import com.isao.spacecards.liked.LikedViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.koin.ksp.generated.defaultModule

val likedModule = module {
  includes(defaultModule)
  viewModelOf(::LikedViewModel)
}
