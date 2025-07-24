// com/feedlove/app/navigation/MainTab.kt
package com.feedlove.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class MainTab(
    val route: String,
    val title: String,
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector
) {
    object Home : MainTab("home", "Home", Icons.Filled.Home, Icons.Outlined.Home)
    object Share : MainTab("share", "Share", Icons.Filled.AddCircle, Icons.Outlined.AddCircle)
    object Profile : MainTab("profile", "Profile", Icons.Filled.Person, Icons.Outlined.Person)

    companion object {
        val allTabs = listOf(Home, Share, Profile)
    }
}