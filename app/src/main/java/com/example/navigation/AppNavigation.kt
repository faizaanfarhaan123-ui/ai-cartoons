package com.example.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.PlayCircleOutline
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.MainViewModel
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.VideosScreen
import com.example.ui.screens.FeedbackAccountScreen
import com.example.ui.screens.SalesScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.AdminUploadScreen

import com.example.ui.screens.VideoPlayerScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument

import com.example.ui.screens.ProfileScreen

@Composable
fun AppNavigation(viewModel: MainViewModel = viewModel()) {
    val navController = rememberNavController()
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    val showBottomBar = currentDestination?.route != Screen.Login.route && currentDestination?.route != Screen.Search.route && currentDestination?.route != Screen.AdminUpload.route && currentDestination?.route?.startsWith("video_player") == false

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                ) {
                    val colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        selectedTextColor = MaterialTheme.colorScheme.secondary,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        indicatorColor = MaterialTheme.colorScheme.secondary
                    )
                    NavigationBarItem(
                        colors = colors,
                        icon = {
                            Icon(
                                imageVector = if (currentDestination?.route == Screen.Dashboard.route) Icons.Filled.Link else Icons.Outlined.Link,
                                contentDescription = "Dashboard"
                            )
                        },
                        label = { Text("Dashboard") },
                        selected = currentDestination?.route == Screen.Dashboard.route,
                        onClick = {
                            navController.navigate(Screen.Dashboard.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                    NavigationBarItem(
                        colors = colors,
                        icon = {
                            Icon(
                                imageVector = if (currentDestination?.route == Screen.Videos.route) Icons.Filled.PlayCircle else Icons.Outlined.PlayCircleOutline,
                                contentDescription = "Videos"
                            )
                        },
                        label = { Text("Videos") },
                        selected = currentDestination?.route == Screen.Videos.route,
                        onClick = {
                            navController.navigate(Screen.Videos.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                    NavigationBarItem(
                        colors = colors,
                        icon = {
                            Icon(
                                imageVector = if (currentDestination?.route == Screen.Feedback.route) Icons.Filled.Person else Icons.Outlined.PersonOutline,
                                contentDescription = "Feedback"
                            )
                        },
                        label = { Text("Feedback") },
                        selected = currentDestination?.route == Screen.Feedback.route,
                        onClick = {
                            navController.navigate(Screen.Feedback.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                    NavigationBarItem(
                        colors = colors,
                        icon = {
                            Icon(
                                imageVector = if (currentDestination?.route == Screen.Sales.route) Icons.Filled.Storefront else Icons.Outlined.Storefront,
                                contentDescription = "Sales"
                            )
                        },
                        label = { Text("Sales") },
                        selected = currentDestination?.route == Screen.Sales.route,
                        onClick = {
                            navController.navigate(Screen.Sales.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Login.route) {
                LoginScreen(viewModel = viewModel, onLoginSuccess = { isAdmin ->
                    viewModel.setAdmin(isAdmin)
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                })
            }
            composable(Screen.Dashboard.route) {
                DashboardScreen(viewModel, navController)
            }
            composable(Screen.Videos.route) {
                VideosScreen(viewModel, navController)
            }
            composable(Screen.Feedback.route) {
                FeedbackAccountScreen(viewModel)
            }
            composable(Screen.Sales.route) {
                SalesScreen(viewModel)
            }
            composable(Screen.Profile.route) {
                ProfileScreen(viewModel, navController)
            }
            composable(Screen.Search.route) {
                SearchScreen(viewModel, navController)
            }
            composable(Screen.AdminUpload.route) {
                AdminUploadScreen(viewModel, navController)
            }
            composable(
                route = Screen.VideoPlayer.route,
                arguments = listOf(navArgument("videoId") { type = NavType.StringType })
            ) { backStackEntry ->
                val videoId = backStackEntry.arguments?.getString("videoId") ?: return@composable
                VideoPlayerScreen(viewModel, navController, videoId)
            }
        }
    }
}
