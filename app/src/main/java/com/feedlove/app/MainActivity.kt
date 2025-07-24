package com.feedlove.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.feedlove.app.navigation.FeedloveNavGraph
import com.feedlove.app.navigation.Screen
import com.feedlove.app.ui.theme.FeedloveTheme
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            FeedloveTheme(useDarkTheme = false) {
                val navController = rememberNavController()
                val isLoggedIn = FirebaseAuth.getInstance().currentUser != null

                FeedloveNavGraph(
                    navController = navController,
                    startDestination = if (isLoggedIn) Screen.Home.route else Screen.Login.route
                )
            }
        }
    }
}