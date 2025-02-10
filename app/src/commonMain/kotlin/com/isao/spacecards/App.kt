package com.isao.spacecards

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.isao.spacecards.feature.designsystem.TopLevelScreen
import com.isao.spacecards.feature.designsystem.theme.LightColorScheme
import com.isao.spacecards.feature.designsystem.theme.SpaceCardsTheme
import com.isao.spacecards.feature.designsystem.theme.Typography
import com.isao.spacecards.feature.feed.navigation.addFeedScreen
import com.isao.spacecards.liked.navigation.addLikedScreen
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.KoinContext

@Composable
fun App(
  modifier: Modifier = Modifier,
  colorScheme: ColorScheme = LightColorScheme,
  typography: Typography = Typography,
) {
  SpaceCardsTheme(colorScheme, typography) {
    Surface(
      modifier = modifier.fillMaxSize(),
      color = MaterialTheme.colorScheme.background,
    ) {
      // Needed as a workaround for some crashes (mostly in tests)
      // https://github.com/InsertKoinIO/koin/issues/1557
      KoinContext {
        val navController = rememberNavController()
        val topLevelScreens = TopLevelScreen.entries

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val destination = navBackStackEntry?.destination
        NavigationSuiteScaffold(
          navigationSuiteItems = {
            topLevelScreens.forEach { screen ->
              item(
                icon = { Icon(imageVector = screen.icon, contentDescription = null) },
                label = { Text(stringResource(screen.nameRes)) },
                selected = destination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = { onTopLevelDestinationClick(screen, navController) },
              )
            }
          },
        ) {
          NavHost(
            navController = navController,
            startDestination = TopLevelScreen.Feed.route,
            modifier = Modifier.fillMaxSize(),
          ) {
            addFeedScreen()
            addLikedScreen()
          }
        }
      }
    }
  }
}

private fun onTopLevelDestinationClick(
  screen: TopLevelScreen,
  navController: NavHostController,
) {
  navController.navigate(screen.route) {
    // Pop up to the start destination of the graph to
    // avoid building up a large stack of destinations
    // on the back stack as users select items
    popUpTo(navController.graph.findStartDestination().route!!) {
      saveState = true
    }
    // Avoid multiple copies of the same destination when
    // reselecting the same item
    launchSingleTop = true
    // Restore state when reselecting a previously selected item
    restoreState = true
  }
}
