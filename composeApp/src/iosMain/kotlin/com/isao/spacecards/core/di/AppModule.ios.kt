package com.isao.spacecards.core.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.isao.spacecards.app.data.Database
import org.koin.core.scope.Scope

actual fun Scope.provideSqlDelightDriver(): SqlDriver =
  NativeSqliteDriver(Database.Schema, "app.db")
