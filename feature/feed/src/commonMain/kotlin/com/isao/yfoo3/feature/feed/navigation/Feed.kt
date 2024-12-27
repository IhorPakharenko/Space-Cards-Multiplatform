package com.isao.yfoo3.feature.feed.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.isao.yfoo3.core.designsystem.TopLevelScreen
import com.isao.yfoo3.feature.feed.composable.FeedRoute

fun NavGraphBuilder.addFeedScreen() {
  composable(TopLevelScreen.Feed.route) {
    FeedRoute()
  }
}
