package com.feedlove.app.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.feedlove.app.navigation.MainTab
import com.feedlove.app.navigation.Screen
import com.feedlove.app.ui.components.BottomNavigationBar
import com.feedlove.app.viewmodel.ProfileViewModel
import com.feedlove.app.viewmodel.ShareFoodViewModel

@Composable
fun MainScreen(
    rootNavController: NavHostController,
    onSignedOut: () -> Unit
) {
    val nestedNavController = rememberNavController()
    val profileVM = hiltViewModel<ProfileViewModel>()
    val shareVM   = hiltViewModel<ShareFoodViewModel>()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = nestedNavController)
        }
    ) { innerPadding ->
        NavHost(
            navController    = nestedNavController,
            startDestination = MainTab.Home.route,
            modifier         = Modifier.padding(innerPadding)
        ) {
            composable(MainTab.Home.route) {
                HomeScreen(
                    navController     = nestedNavController,
                    rootNavController = rootNavController,
                    profileVM         = profileVM,
                    shareFoodVM       = shareVM
                )
            }
            composable(MainTab.Share.route) {
                ShareFoodScreen(
                    navController = nestedNavController,
                    viewModel     = shareVM
                )
            }
            composable(MainTab.Profile.route) {
                ProfileScreen(
                    navController = nestedNavController,
                    viewModel     = profileVM,
                    onSignedOut   = { onSignedOut() }
                )
            }
            composable(Screen.Settings.route) {
                SettingsScreen(
                    nestedNavController = nestedNavController,
                    rootNavController = rootNavController,
                    onBack = { nestedNavController.popBackStack() }
                )
            }
        }
    }
}
