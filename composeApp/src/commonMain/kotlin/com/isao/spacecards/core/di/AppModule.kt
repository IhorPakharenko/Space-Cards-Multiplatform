package com.isao.spacecards.core.di

import com.isao.spacecards.data.di.databaseModule
import com.isao.spacecards.feature.feed.di.feedModule
import com.isao.spacecards.liked.di.likedModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.dsl.module
import org.koin.ksp.generated.defaultModule
import org.koin.ksp.generated.module

val appModule = module {
    includes(
        databaseModule,
        defaultModule,
        feedModule,
        likedModule,
        MyModule().module,
        MyModule2().module
    )
}

//TODO remove below
@Module
@ComponentScan("com.isao.spacecards")
class MyModule

@Module
@ComponentScan
class MyModule2