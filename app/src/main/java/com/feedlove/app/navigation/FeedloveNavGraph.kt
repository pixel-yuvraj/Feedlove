package com.feedlove.app.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.feedlove.app.ui.screens.*
import com.feedlove.app.viewmodel.*

@Composable
fun FeedloveNavGraph(
    navController: NavHostController,
    startDestination: String
) {
    val authVM    = hiltViewModel<AuthViewModel>()
    val profileVM = hiltViewModel<ProfileViewModel>()
    val shareVM   = hiltViewModel<ShareFoodViewModel>()

    NavHost(navController = navController, startDestination = startDestination) {

        composable(Screen.Login.route) {
            LoginScreen(navController, authVM)
        }

        composable(Screen.SignUp.route) {
            SignUpScreen(navController, authVM)
        }

        composable(Screen.Home.route) {
            MainScreen(
                onSignedOut = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                rootNavController = navController
            )
        }

        composable(
            route = Screen.BorrowFood.route + "/{listingId}",
            arguments = listOf(navArgument("listingId") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            BorrowFoodScreen(
                navController = navController,
                navBackStackEntry = backStackEntry,
                viewModel = shareVM
            )
        }

        composable(Screen.ShareFood.route) {
            ShareFoodScreen(navController, shareVM)
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                nestedNavController = navController,
                rootNavController = navController,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Chat.route) {
            ChatScreen(navController = navController)
        }

        composable(Screen.PrivacyPolicy.route) {
            PrivacyPolicyScreen(
                navController = navController,
                url = "https://pixel-yuvraj.github.io/privacy_policy.html"
            )
        }
    }
}