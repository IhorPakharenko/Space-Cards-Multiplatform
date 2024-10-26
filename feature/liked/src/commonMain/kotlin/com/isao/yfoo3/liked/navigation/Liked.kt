package com.isao.yfoo3.liked.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.isao.yfoo3.core.designsystem.BottomNavigationScreen
import com.isao.yfoo3.liked.composable.LikedRoute

fun NavGraphBuilder.addLikedScreen() {
    composable(BottomNavigationScreen.Liked.route) {
        LikedRoute()
    }
}