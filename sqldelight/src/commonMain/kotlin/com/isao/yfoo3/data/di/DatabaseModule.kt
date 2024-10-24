package com.isao.yfoo3.data.di

import com.isao.yfoo3.data.Database
import com.isao.yfoo3.data.SqlDelightDriverFactory
import com.isao.yfoo3.data.dao.FeedImageDao
import com.isao.yfoo3.data.dao.FeedImageDaoSqlDelight
import com.isao.yfoo3.data.dao.LikedImageDao
import com.isao.yfoo3.data.dao.LikedImageDaoSqlDelight
import org.koin.dsl.module

val databaseModule = module {
    single<Database> {
        val driver = get<SqlDelightDriverFactory>().createDriver()
        Database(driver)
    }
    single<FeedImageDao> {
        FeedImageDaoSqlDelight(get<Database>().feedImageCachedQueries)
    }
    single<LikedImageDao> {
        LikedImageDaoSqlDelight(get<Database>().likedImageCachedQueries)
    }
}