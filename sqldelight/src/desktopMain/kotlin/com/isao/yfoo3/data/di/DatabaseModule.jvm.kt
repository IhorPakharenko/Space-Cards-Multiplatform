package com.isao.yfoo3.data.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.isao.yfoo3.data.Database
import org.koin.core.scope.Scope

actual fun Scope.provideSqlDelightDriver(): SqlDriver {
    val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
    Database.Schema.create(driver)
    return driver
}