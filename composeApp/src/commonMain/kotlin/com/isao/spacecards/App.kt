package com.isao.spacecards

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Typography
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.isao.spacecards.core.designsystem.LocalWindowSizeClass
import com.isao.spacecards.core.designsystem.TopLevelScreen
import com.isao.spacecards.core.designsystem.getWindowSizeClass
import com.isao.spacecards.core.designsystem.theme.LightColorScheme
import com.isao.spacecards.core.designsystem.theme.SpaceCardsTheme
import com.isao.spacecards.core.designsystem.theme.Typography
import com.isao.spacecards.core.navigation.SpaceCardsNavHost
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.KoinContext

@Composable
fun App(
  modifier: Modifier = Modifier,
  colorScheme: ColorScheme = LightColorScheme,
  typography: Typography = Typography,
  windowSizeClass: WindowSizeClass = getWindowSizeClass(),
) {
  SpaceCardsTheme(colorScheme, typography) {
    SpaceCardsTheme {
      Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
      ) {
        // Needed as a workaround for some crashes (mostly in tests)
        // https://github.com/InsertKoinIO/koin/issues/1557
        KoinContext {
          val navController = rememberNavController()
          val topLevelScreens = TopLevelScreen.entries

          Scaffold(
            bottomBar = {
              if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                BottomNavigationBar(navController, topLevelScreens)
              }
            },
            // Let the content take up all available space.
            // Material3 components handle the insets themselves
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
          ) { padding ->
            CompositionLocalProvider(
              LocalWindowSizeClass provides windowSizeClass,
            ) {
              Row {
                if (windowSizeClass.widthSizeClass !=
                  WindowWidthSizeClass.Compact
                ) {
                  NavigationRail(navController, topLevelScreens)
                }
                SpaceCardsNavHost(
                  navController = navController,
                  modifier = Modifier
                    .fillMaxHeight()
                    .padding(padding),
                )
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun BottomNavigationBar(
  navController: NavHostController,
  topLevelScreens: List<TopLevelScreen>,
) {
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentDestination = navBackStackEntry?.destination

  NavigationBar {
    topLevelScreens.forEach { screen ->
      NavigationBarItem(
        icon = { Icon(imageVector = screen.icon, contentDescription = null) },
        label = { Text(stringResource(screen.nameRes)) },
        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
        onClick = { onTopLevelDestinationClick(screen, navController) },
      )
    }
  }
}

@Composable
private fun NavigationRail(
  navController: NavHostController,
  screens: List<TopLevelScreen>,
) {
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentDestination = navBackStackEntry?.destination

  NavigationRail(Modifier.padding(top = 80.dp)) {
    screens.forEach { screen ->
      NavigationRailItem(
        icon = { Icon(imageVector = screen.icon, contentDescription = null) },
        label = { Text(stringResource(screen.nameRes)) },
        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
        onClick = { onTopLevelDestinationClick(screen, navController) },
      )
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
