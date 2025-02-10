package com.isao.spacecards.feature.feed.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.isao.spacecards.feature.designsystem.TopLevelScreen
import com.isao.spacecards.feature.feed.composable.FeedRoute

fun NavGraphBuilder.addFeedScreen() {
  composable(TopLevelScreen.Feed.route) {
    FeedRoute()
  }
}
