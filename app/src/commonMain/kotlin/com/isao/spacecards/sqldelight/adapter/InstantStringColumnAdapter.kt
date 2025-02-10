package com.isao.spacecards.sqldelight.adapter

import app.cash.sqldelight.ColumnAdapter
import kotlinx.datetime.Instant

internal object InstantStringColumnAdapter : ColumnAdapter<Instant, String> {
  override fun decode(databaseValue: String): Instant = Instant.parse(databaseValue)

  override fun encode(value: Instant): String = value.toString()
}
