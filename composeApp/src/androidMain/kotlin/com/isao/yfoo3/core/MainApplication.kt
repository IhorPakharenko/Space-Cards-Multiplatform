package com.isao.yfoo3.core

import android.app.Application
import co.touchlab.kermit.Logger
import co.touchlab.kermit.koin.KermitKoinLogger
import com.isao.yfoo3.core.di.appModule

import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module


class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            logger(KermitKoinLogger(Logger.withTag("koin")))
            androidContext(this@MainApplication)
            modules(appModule)
        }

            //TODO check if kermit disables logs in release by default
//        if (BuildConfig.DEBUG) {
//            Timber.plant(Timber.DebugTree())
//        }
    }
}