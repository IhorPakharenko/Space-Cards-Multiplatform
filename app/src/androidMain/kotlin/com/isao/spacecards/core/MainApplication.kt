package com.isao.spacecards.core

import android.app.Application
import co.touchlab.kermit.Logger
import co.touchlab.kermit.koin.KermitKoinLogger
import com.isao.spacecards.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    startKoin {
      logger(KermitKoinLogger(Logger.withTag("koin")))
      androidContext(this@MainApplication)
      modules(appModule)
    }
  }
}
