package com.isao.spacecards

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import com.isao.spacecards.app.data.Database
import com.isao.spacecards.astrobinimages.data.AstrobinImageEntity
import com.isao.spacecards.component.astrobinimages.astrobinImagesComponentModule
import com.isao.spacecards.core.ktor.di.ktorModule
import com.isao.spacecards.feature.feed.di.feedModule
import com.isao.spacecards.liked.di.likedModule
import com.isao.spacecards.sqldelight.adapter.InstantStringColumnAdapter
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
        publishedAtAdapter = InstantStringColumnAdapter,
        updatedAtAdapter = InstantStringColumnAdapter,
        uploadedAtAdapter = InstantStringColumnAdapter,
        bookmarkedAtAdapter = InstantStringColumnAdapter,
        viewedAtAdapter = InstantStringColumnAdapter,
      ),
    )
  }
  single { get<Database>().astrobinImageEntityQueries }
  includes(
    ktorModule,
    feedModule,
    likedModule,
    astrobinImagesComponentModule,
  )
}

expect fun Scope.provideSqlDelightDriver(): SqlDriver
