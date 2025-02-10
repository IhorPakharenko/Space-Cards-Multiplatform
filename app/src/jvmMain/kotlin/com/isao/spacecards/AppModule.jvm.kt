package com.isao.spacecards

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.isao.spacecards.app.data.Database
import org.koin.core.scope.Scope
import java.io.File

actual fun Scope.provideSqlDelightDriver(): SqlDriver = JdbcSqliteDriver(
  url = "jdbc:sqlite:${getDatabasePath()}",
  schema = Database.Schema,
)

private fun getDatabasePath(): String {
  val userHome = System.getProperty("user.home")
  val dbDirectory = File(userHome, ".spacecards")
  dbDirectory.mkdirs()

  return File(dbDirectory, "spacecards.db").absolutePath
}
