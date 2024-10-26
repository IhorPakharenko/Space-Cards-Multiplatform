package com.isao.yfoo3.feature.feed.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.isao.yfoo3.core.designsystem.BottomNavigationScreen
import com.isao.yfoo3.feature.feed.composable.FeedRoute

fun NavGraphBuilder.addFeedScreen() {
    composable(BottomNavigationScreen.Feed.route) {
        FeedRoute()
    }
}