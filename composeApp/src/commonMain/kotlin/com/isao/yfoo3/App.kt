package com.isao.yfoo3

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.isao.yfoo3.core.designsystem.BottomNavigationScreen
import com.isao.yfoo3.core.designsystem.theme.LightColorScheme
import com.isao.yfoo3.core.designsystem.theme.Typography
import com.isao.yfoo3.core.designsystem.theme.Yfoo2Theme
import com.isao.yfoo3.core.navigation.YfooNavHost
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext


@Composable
@Preview
fun App(
    colorScheme: ColorScheme = LightColorScheme,
    typography: Typography = Typography
) {
    Yfoo2Theme(colorScheme, typography) {
        Yfoo2Theme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = androidx.compose.material3.MaterialTheme.colorScheme.background
            ) {
                val navController = rememberNavController()
                val bottomNavigationScreens = remember {
                    listOf(
                        BottomNavigationScreen.Feed,
                        BottomNavigationScreen.Liked
                    )
                }

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination

                            bottomNavigationScreens.forEach { screen ->
                                NavigationBarItem(
                                    icon = {
                                        Icon(
                                            imageVector = screen.icon,
                                            contentDescription = null
                                        )
                                    },
                                    label = { Text(stringResource(screen.nameRes)) },
                                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                    onClick = {
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
                                )
                            }
                        }
                    },
                    // Let the content take up all available space.
                    // Material3 components handle the insets themselves
                    contentWindowInsets = WindowInsets(0, 0, 0, 0)
                ) { padding ->
                    // Needed as a workaround for some crashes (mostly in tests)
                    // https://github.com/InsertKoinIO/koin/issues/1557
                    KoinContext {
                        YfooNavHost(
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