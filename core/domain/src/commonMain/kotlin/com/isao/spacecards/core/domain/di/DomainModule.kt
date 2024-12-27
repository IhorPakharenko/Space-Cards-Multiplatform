package com.isao.spacecards.core.domain.di

import com.isao.spacecards.core.data.di.dataModule
import org.koin.dsl.module
import org.koin.ksp.generated.defaultModule

// @Module
// @ComponentScan("com.isao.spacecards.core.domain")
// internal class DomainModule
//
val domainModule = module {
  includes(defaultModule)
  includes(dataModule)
}
