package com.isao.spacecards.core.designsystem

sealed class Screen(val route: String) {
  object Feed : Screen("feed")

  object Liked : Screen("liked")
}
