package com.isao.spacecards.data.testdoubles

import com.isao.spacecards.data.dao.FeedImageDao
import com.isao.spacecards.data.model.FeedImageCached
import kotlinx.coroutines.flow.Flow

class FakeFeedImageDao : FeedImageDao {
    override fun getFeedImages(): Flow<List<FeedImageCached>> {
        throw NotImplementedError() // Stub
    }

    override fun getFeedImage(id: String): Flow<FeedImageCached> {
        throw NotImplementedError() // Stub
    }

    override suspend fun saveFeedImage(item: FeedImageCached): Long {
        throw NotImplementedError() // Stub
    }

    override suspend fun deleteFeedImage(id: String) {
        throw NotImplementedError() // Stub
    }
}