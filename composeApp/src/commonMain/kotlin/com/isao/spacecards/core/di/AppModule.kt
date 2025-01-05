package com.isao.spacecards.core.di

import com.isao.spacecards.component.images.di.imagesModule
import com.isao.spacecards.data.di.databaseModule
import com.isao.spacecards.feature.feed.di.feedModule
import com.isao.spacecards.liked.di.likedModule
import org.koin.dsl.module

val appModule = module {
  includes(
    databaseModule,
    feedModule,
    likedModule,
    imagesModule,
  )
}
