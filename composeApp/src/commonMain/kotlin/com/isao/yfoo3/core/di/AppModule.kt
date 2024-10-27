package com.isao.yfoo3.core.di

import com.isao.yfoo3.data.di.databaseModule
import com.isao.yfoo3.feature.feed.di.feedModule
import com.isao.yfoo3.liked.di.likedModule
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
@ComponentScan("com.isao.yfoo3")
class MyModule

@Module
@ComponentScan
class MyModule2