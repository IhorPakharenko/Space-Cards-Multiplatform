package com.isao.spacecards.component.common

import androidx.paging.PagingSource
import app.cash.sqldelight.Query
import kotlin.properties.Delegates

/**
 * Shamelessly stolen from Tivi with minor changes: https://github.com/chrisbanes/tivi
 */
abstract class QueryPagingSource<Key : Any, RowType : Any> :
  PagingSource<Key, RowType>(),
  Query.Listener {
  protected var currentQuery: Query<*>? by Delegates.observable(null) { _, old, new ->
    old?.removeListener(this)
    new?.addListener(this)
  }

  init {
    registerInvalidatedCallback {
      currentQuery?.removeListener(this)
      currentQuery = null
    }
  }

  final override fun queryResultsChanged() = invalidate()
}
