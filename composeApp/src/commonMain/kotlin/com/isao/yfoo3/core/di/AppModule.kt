package com.isao.yfoo3.core.di

import com.isao.yfoo3.data.di.databaseModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.dsl.module
import org.koin.ksp.generated.defaultModule

//import org.koin.ksp.generated.*

val appModule = module {
    includes(databaseModule, defaultModule)
}

@Module
@ComponentScan("com.isao.yfoo3")
class MyModule