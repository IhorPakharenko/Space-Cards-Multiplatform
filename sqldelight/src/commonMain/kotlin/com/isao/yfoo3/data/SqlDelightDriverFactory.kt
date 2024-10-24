package com.isao.yfoo3.data

import app.cash.sqldelight.db.SqlDriver
import org.koin.core.annotation.Single

@Single
expect class SqlDelightDriverFactory {
    fun createDriver(): SqlDriver
}