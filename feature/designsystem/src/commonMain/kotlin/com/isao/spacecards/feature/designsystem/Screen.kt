package com.isao.spacecards.feature.designsystem

sealed class Screen(val route: String) {
  object Feed : Screen("feed")

  object Liked : Screen("liked")
}
