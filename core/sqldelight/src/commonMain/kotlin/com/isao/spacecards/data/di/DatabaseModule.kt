package com.isao.spacecards.data.di

import app.cash.sqldelight.db.SqlDriver
import com.isao.spacecards.core.data.Database
import com.isao.spacecards.core.db.dao.FeedImageDao
import com.isao.spacecards.core.db.dao.LikedImageDao
import com.isao.spacecards.data.dao.FeedImageDaoSqlDelight
import com.isao.spacecards.data.dao.LikedImageDaoSqlDelight
import org.koin.core.scope.Scope
import org.koin.dsl.module

val databaseModule = module {
  single<Database> {
    Database(provideSqlDelightDriver())
  }
  single<FeedImageDao> {
    FeedImageDaoSqlDelight(get<Database>().feedImageCachedQueries)
  }
  single<LikedImageDao> {
    LikedImageDaoSqlDelight(get<Database>().likedImageCachedQueries)
  }
}

expect fun Scope.provideSqlDelightDriver(): SqlDriver
