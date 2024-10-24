package com.isao.yfoo3.data.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.isao.yfoo3.data.Database
import org.koin.core.scope.Scope

actual fun Scope.provideSqlDelightDriver(): SqlDriver {
    return NativeSqliteDriver(Database.Schema, "test.db")
}