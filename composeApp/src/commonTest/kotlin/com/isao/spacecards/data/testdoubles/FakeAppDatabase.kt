package com.isao.spacecards.data.testdoubles

import androidx.room.InvalidationTracker
import com.isao.spacecards.core.database.AppDatabase
import com.isao.spacecards.data.dao.FeedImageDao
import com.isao.spacecards.data.dao.LikedImageDao
import org.koin.core.annotation.Single

@Single(binds = [AppDatabase::class])
class FakeAppDatabase : AppDatabase(), FakeDbFix {
    override fun feedImageDao(): FeedImageDao {
        throw NotImplementedError() // Stub
    }

    override fun likedImageDao(): LikedImageDao {
        throw NotImplementedError() // Stub
    }

    override fun createInvalidationTracker(): InvalidationTracker {
        throw NotImplementedError() // Stub
    }

    override fun clearAllTables() {
        super.clearAllTables()
    }
}

interface FakeDbFix {
    fun clearAllTables() {

    }
}