// com/feedlove/app/ui/screens/SplashScreen.kt
package com.feedlove.app.ui.screens

import android.window.SplashScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.feedlove.app.R
import com.feedlove.app.navigation.Screen
import kotlinx.coroutines.delay
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(true) {
        delay(2000) // 2 second splash
        val user = FirebaseAuth.getInstance().currentUser
        navController.navigate(if (user != null) Screen.Home.route else Screen.Login.route) {
            popUpTo(Screen.Splash.route) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_feedlove),
            contentDescription = "FeedLove Logo",
            modifier = Modifier.size(120.dp)
        )
    }
}