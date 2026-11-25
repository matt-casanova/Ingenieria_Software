package com.example.ingesoftware.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ingesoftware.ui.screens.HelpDetailScreen
import com.example.ingesoftware.ui.screens.HelpInfoScreen
import com.example.ingesoftware.ui.screens.HelpScreen
import com.example.ingesoftware.ui.screens.HomeScreen
import com.example.ingesoftware.ui.screens.PlazaInfoScreen
import com.example.ingesoftware.ui.screens.SafePointsScreen
import com.example.ingesoftware.ui.theme.PetroleumBlue
import com.example.ingesoftware.ui.theme.PetroleumBlueContainer

sealed class Screen(val route: String, val title: String, val icon: ImageVector? = null) {
    object Home : Screen("inicio", "Inicio", Icons.Default.Home)
    object SafePoints : Screen("puntos_seguros", "Puntos seguros", Icons.Default.Place)
    object PlazaInfo : Screen("informacion_plaza", "Información", Icons.Default.Info)

    object Help : Screen("solicitar_ayuda", "Solicitar ayuda")
    object HelpDetail : Screen("detalle_solicitud?tipo={tipo}", "Detalle de asistencia") {
        fun createRoute(tipo: String) = "detalle_solicitud?tipo=$tipo"
    }
    object HelpInfo : Screen("informacion_asistencia", "Información de asistencia")
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    val bottomBarScreens = listOf(
        Screen.Home,
        Screen.SafePoints,
        Screen.PlazaInfo
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in bottomBarScreens.map { it.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    bottomBarScreens.forEach { screen ->
                        val selected = currentRoute == screen.route
                        NavigationBarItem(
                            icon = {
                                screen.icon?.let { icon ->
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = screen.title
                                    )
                                }
                            },
                            label = {
                                Text(
                                    text = screen.title,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                                    )
                                )
                            },
                            selected = selected,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = PetroleumBlue,
                                selectedTextColor = PetroleumBlue,
                                indicatorColor = PetroleumBlueContainer
                            ),
                            onClick = {
                                if (currentRoute != screen.route) {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onRequestHelpClick = {
                        navController.navigate(Screen.Help.route)
                    },
                    onSafePointsClick = {
                        navController.navigate(Screen.SafePoints.route)
                    },
                    onPlazaInfoClick = {
                        navController.navigate(Screen.PlazaInfo.route)
                    }
                )
            }

            composable(Screen.SafePoints.route) {
                SafePointsScreen()
            }

            composable(Screen.PlazaInfo.route) {
                PlazaInfoScreen()
            }

            composable(Screen.Help.route) {
                HelpScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onContinueClick = { selectedTitle ->
                        navController.navigate(Screen.HelpDetail.createRoute(selectedTitle))
                    }
                )
            }

            composable(
                route = Screen.HelpDetail.route,
                arguments = listOf(
                    navArgument("tipo") {
                        type = NavType.StringType
                        defaultValue = "Me siento incómodo o inseguro"
                    }
                )
            ) { backStackEntry ->
                val selectedType = backStackEntry.arguments?.getString("tipo") ?: "Me siento incómodo o inseguro"
                HelpDetailScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onNextStepClick = {
                        navController.navigate(Screen.HelpInfo.route)
                    },
                    initialSituationType = selectedType
                )
            }

            composable(Screen.HelpInfo.route) {
                HelpInfoScreen(
                    onReturnHomeClick = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
    }
}
