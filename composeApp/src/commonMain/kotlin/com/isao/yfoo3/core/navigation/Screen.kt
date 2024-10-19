package com.isao.yfoo3.core.navigation

sealed class Screen(val route: String) {
    object Feed : Screen("feed")
    object Liked : Screen("liked")
}
