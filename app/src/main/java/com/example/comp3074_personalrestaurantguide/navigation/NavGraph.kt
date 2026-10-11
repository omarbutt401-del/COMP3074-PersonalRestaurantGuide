package com.example.comp3074_personalrestaurantguide.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.comp3074_personalrestaurantguide.ui.screens.AboutScreen
import com.example.comp3074_personalrestaurantguide.ui.screens.AddEditRestaurantScreen
import com.example.comp3074_personalrestaurantguide.ui.screens.DetailsScreen
import com.example.comp3074_personalrestaurantguide.ui.screens.FullMapScreen
import com.example.comp3074_personalrestaurantguide.ui.screens.RestaurantListScreen
import com.example.comp3074_personalrestaurantguide.ui.screens.SplashScreen

object Routes {
    const val SPLASH = "splash"
    const val LIST = "list"
    const val ADD = "add"
    const val EDIT = "edit/{id}"
    const val DETAILS = "details/{id}"
    const val MAP = "map/{id}"
    const val ABOUT = "about"
    fun edit(id: Long) = "edit/$id"
    fun details(id: Long) = "details/$id"
    fun map(id: Long) = "map/$id"
}

@Composable
fun AppNavGraph() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = Routes.SPLASH) {

        composable(Routes.SPLASH) {
            SplashScreen(onFinished = {
                nav.navigate(Routes.LIST) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            })
        }
        composable(Routes.LIST) {
            RestaurantListScreen(
                onAdd = { nav.navigate(Routes.ADD) },
                onOpen = { id -> nav.navigate(Routes.details(id)) },
                onAbout = { nav.navigate(Routes.ABOUT) }
            )
        }
        composable(Routes.ADD) {
            AddEditRestaurantScreen(
                restaurantId = null,
                onSaved = { nav.popBackStack() },
                onCancel = { nav.popBackStack() }
            )
        }
        composable(
            Routes.EDIT,
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { entry ->
            AddEditRestaurantScreen(
                restaurantId = entry.arguments?.getLong("id"),
                onSaved = { nav.popBackStack() },
                onCancel = { nav.popBackStack() }
            )
        }
        composable(
            Routes.DETAILS,
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { entry ->
            DetailsScreen(
                restaurantId = entry.arguments!!.getLong("id"),
                onBack = { nav.popBackStack() },
                onEdit = { id -> nav.navigate(Routes.edit(id)) },
                onViewMap = { id -> nav.navigate(Routes.map(id)) }
            )
        }
        composable(
            Routes.MAP,
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { entry ->
            FullMapScreen(
                restaurantId = entry.arguments!!.getLong("id"),
                onBack = { nav.popBackStack() }
            )
        }
        composable(Routes.ABOUT) {
            AboutScreen(onBack = { nav.popBackStack() })
        }
    }
}