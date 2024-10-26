package com.isao.yfoo3.core.designsystem

sealed class Screen(val route: String) {
    object Feed : Screen("feed")
    object Liked : Screen("liked")
}
