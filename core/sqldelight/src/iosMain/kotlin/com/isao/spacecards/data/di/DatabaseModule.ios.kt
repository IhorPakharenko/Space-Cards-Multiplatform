package com.isao.spacecards.data.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.isao.spacecards.data.Database
import org.koin.core.scope.Scope

actual fun Scope.provideSqlDelightDriver(): SqlDriver =
  NativeSqliteDriver(Database.Schema, "test.db")
