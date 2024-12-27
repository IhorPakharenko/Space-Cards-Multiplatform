package com.isao.spacecards.utils

//import org.junit.rules.TestRule
//import org.junit.rules.TestWatcher
//import org.junit.runner.Description
//import org.koin.core.context.startKoin
//import org.koin.core.context.stopKoin
//import org.koin.core.module.Module
//
//class KoinRule(private val modules: List<Module>) : TestWatcher(), TestRule {
//    override fun starting(description: Description?) {
//        super.starting(description)
//        stopKoin()
//        startKoin { modules(modules) }
//    }
//
//    override fun finished(description: Description?) {
//        super.finished(description)
//        stopKoin()
//    }
//}