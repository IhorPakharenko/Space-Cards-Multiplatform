package com.isao.spacecards.core.di

import app.cash.sqldelight.ColumnAdapter
import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import com.isao.spacecards.app.data.Database
import com.isao.spacecards.astrobinimages.data.AstrobinImageEntity
import com.isao.spacecards.component.astrobinimages.astrobinImagesModule
import com.isao.spacecards.component.images.di.imagesModule
import com.isao.spacecards.core.db.dao.FeedImageDao
import com.isao.spacecards.core.db.dao.LikedImageDao
import com.isao.spacecards.core.ktor.di.ktorModule
import com.isao.spacecards.data.dao.FeedImageDaoSqlDelight
import com.isao.spacecards.data.dao.LikedImageDaoSqlDelight
import com.isao.spacecards.feature.feed.di.feedModule
import com.isao.spacecards.liked.di.likedModule
import kotlinx.datetime.Instant
import org.koin.core.scope.Scope
import org.koin.dsl.module

val appModule = module {
  single<Database> {
    Database(
      driver = provideSqlDelightDriver(),
      AstrobinImageEntityAdapter = AstrobinImageEntity.Adapter(
        idAdapter = IntColumnAdapter,
        bookmarksAdapter = IntColumnAdapter,
        commentsAdapter = IntColumnAdapter,
        licenseAdapter = IntColumnAdapter,
        likesAdapter = IntColumnAdapter,
        viewsAdapter = IntColumnAdapter,
        publishedAdapter = InstantStringColumnAdapter,
        updatedAdapter = InstantStringColumnAdapter,
        uploadedAdapter = InstantStringColumnAdapter,
        bookmarkedAtAdapter = InstantStringColumnAdapter,
        viewedAtAdapter = InstantStringColumnAdapter,
      ),
    )
  }
  single { get<Database>().astrobinImageEntityQueries }
  single { get<Database>().feedImageCachedQueries }
  single { get<Database>().likedImageCachedQueries }
  single<FeedImageDao> {
    FeedImageDaoSqlDelight(get())
  }
  single<LikedImageDao> {
    LikedImageDaoSqlDelight(get())
  }
  includes(
    ktorModule,
    feedModule,
    likedModule,
    imagesModule,
    astrobinImagesModule,
  )
}

//TODO place elsewhere
internal object InstantStringColumnAdapter : ColumnAdapter<Instant, String> {
  override fun decode(databaseValue: String): Instant = Instant.parse(databaseValue)

  override fun encode(value: Instant): String = value.toString()
}

expect fun Scope.provideSqlDelightDriver(): SqlDriver
