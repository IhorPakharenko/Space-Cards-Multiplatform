package com.isao.spacecards.liked.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.isao.spacecards.core.designsystem.TopLevelScreen
import com.isao.spacecards.liked.composable.LikedRoute

fun NavGraphBuilder.addLikedScreen() {
  composable(TopLevelScreen.Liked.route) {
    LikedRoute()
  }
}
