package com.isao.spacecards.liked.di

import com.isao.spacecards.liked.LikedViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val likedModule = module {
  viewModelOf(::LikedViewModel)
}
