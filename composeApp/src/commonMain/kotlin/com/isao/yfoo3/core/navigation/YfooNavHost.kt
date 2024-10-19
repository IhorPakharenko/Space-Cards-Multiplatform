package com.isao.yfoo3.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.isao.yfoo3.BottomNavigationScreen

@Composable
fun YfooNavHost(
    navController: NavHostController,
    factories: List<NavigationFactory>,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavigationScreen.Feed.route,
        modifier = modifier,
    ) {
        factories.forEach {
            it.create(this)
        }
    }
}
