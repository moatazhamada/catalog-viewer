package com.houseofalgorithms.catalogviewer.ui.navigation

import android.app.Activity
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.houseofalgorithms.catalogviewer.ui.detail.CatalogDetailScreen
import com.houseofalgorithms.catalogviewer.ui.list.CatalogListScreen

sealed class Screen(val route: String) {
    data object List : Screen("catalog_list")

    data object Detail : Screen("catalog_detail/{itemId}") {
        fun createRoute(itemId: String) = "catalog_detail/$itemId"
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    val context = LocalContext.current
    val activity = context as? Activity
    val windowSizeClass =
        if (activity != null) {
            calculateWindowSizeClass(activity)
        } else {
            // Fallback for non-Activity contexts
            androidx.compose.material3.windowsizeclass.WindowSizeClass
                .calculateFromSize(DpSize(360.dp, 640.dp))
        }
    val isExpandedScreen = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded

    if (isExpandedScreen) {
        // Tablet/Foldable: Use two-pane layout
        com.houseofalgorithms.catalogviewer.ui.catalog
            .CatalogScreen(
                onNavigateToDetail = { itemId ->
                    // In two-pane mode, selection is handled internally
                },
            )
    } else {
        // Phone: Use navigation
        // Wrap NavHost in Box with background to prevent white flash during transitions
        androidx.compose.foundation.layout.Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.List.route,
            ) {
                composable(
                    route = Screen.List.route,
                    enterTransition = {
                        fadeIn(animationSpec = tween(300)) +
                            slideInHorizontally(
                                initialOffsetX = { -it },
                                animationSpec = tween(300),
                            )
                    },
                    exitTransition = {
                        fadeOut(animationSpec = tween(300)) +
                            slideOutHorizontally(
                                targetOffsetX = { -it },
                                animationSpec = tween(300),
                            )
                    },
                    popEnterTransition = {
                        fadeIn(animationSpec = tween(300)) +
                            slideInHorizontally(
                                initialOffsetX = { -it },
                                animationSpec = tween(300),
                            )
                    },
                    popExitTransition = {
                        fadeOut(animationSpec = tween(300)) +
                            slideOutHorizontally(
                                targetOffsetX = { -it },
                                animationSpec = tween(300),
                            )
                    },
                ) {
                    CatalogListScreen(
                        onItemClick = { itemId ->
                            navController.navigate(Screen.Detail.createRoute(itemId))
                        },
                    )
                }
                composable(
                    route = Screen.Detail.route,
                    arguments = listOf(navArgument("itemId") { type = NavType.StringType }),
                    enterTransition = {
                        fadeIn(animationSpec = tween(300)) +
                            slideInHorizontally(
                                initialOffsetX = { it },
                                animationSpec = tween(300),
                            )
                    },
                    exitTransition = {
                        fadeOut(animationSpec = tween(300)) +
                            slideOutHorizontally(
                                targetOffsetX = { it },
                                animationSpec = tween(300),
                            )
                    },
                    popEnterTransition = {
                        fadeIn(animationSpec = tween(300)) +
                            slideInHorizontally(
                                initialOffsetX = { -it },
                                animationSpec = tween(300),
                            )
                    },
                    popExitTransition = {
                        fadeOut(animationSpec = tween(300)) +
                            slideOutHorizontally(
                                targetOffsetX = { -it },
                                animationSpec = tween(300),
                            )
                    },
                ) { backStackEntry ->
                    val itemId = backStackEntry.arguments?.getString("itemId") ?: ""
                    CatalogDetailScreen(
                        onBackClick = { navController.popBackStack() },
                        itemId = itemId,
                    )
                }
            }
        }
    }
}
