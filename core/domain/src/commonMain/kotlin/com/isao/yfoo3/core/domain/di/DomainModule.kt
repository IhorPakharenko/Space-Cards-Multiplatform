package com.isao.yfoo3.core.domain.di

import com.isao.yfoo3.core.data.di.dataModule
import org.koin.dsl.module
import org.koin.ksp.generated.defaultModule

// @Module
// @ComponentScan("com.isao.yfoo3.core.domain")
// internal class DomainModule
//
val domainModule = module {
  includes(defaultModule)
  includes(dataModule)
}
