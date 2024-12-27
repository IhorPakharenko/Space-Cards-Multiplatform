package com.isao.spacecards.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.isao.spacecards.core.designsystem.TopLevelScreen
import com.isao.spacecards.feature.feed.navigation.addFeedScreen
import com.isao.spacecards.liked.navigation.addLikedScreen

@Composable
fun SpaceCardsNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = TopLevelScreen.Feed.route,
        modifier = modifier,
    ) {
        addFeedScreen()
        addLikedScreen()
    }
}
