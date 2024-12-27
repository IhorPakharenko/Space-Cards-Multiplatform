package com.isao.spacecards.data.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.isao.spacecards.core.data.Database
import org.koin.core.scope.Scope

actual fun Scope.provideSqlDelightDriver(): SqlDriver =
  AndroidSqliteDriver(Database.Schema, get(), "test.db")
