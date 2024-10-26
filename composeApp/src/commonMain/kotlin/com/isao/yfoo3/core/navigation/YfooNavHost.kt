package com.isao.yfoo3.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.isao.yfoo3.core.designsystem.BottomNavigationScreen
import com.isao.yfoo3.feature.feed.navigation.addFeedScreen
import com.isao.yfoo3.liked.navigation.addLikedScreen

@Composable
fun YfooNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavigationScreen.Feed.route,
        modifier = modifier,
    ) {
        addFeedScreen()
        addLikedScreen()
    }
}
