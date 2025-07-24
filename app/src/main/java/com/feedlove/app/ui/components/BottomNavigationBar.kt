// com/feedlove/app/ui/components/BottomNavigationBar.kt
package com.feedlove.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.feedlove.app.navigation.MainTab

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color(0xFFF8FAFF), // soft white with hint of blue
        tonalElevation = 12.dp             // slight elevation for a pro feel
    ) {
        MainTab.allTabs.forEach { tab ->
            val selected = (currentRoute == tab.route)

            // Icon grows slightly when selected
            val scale by animateFloatAsState(if (selected) 1.2f else 1f, label = "")

            // Icon color: always darkish (onBackground), but more opaque when selected
            val iconColor by animateColorAsState(
                targetValue = if (selected)
                    MaterialTheme.colorScheme.onBackground
                else
                    MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                label = ""
            )

            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(tab.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (selected) tab.filledIcon else tab.outlinedIcon,
                        contentDescription = tab.title,
                        tint = iconColor,
                        modifier = Modifier
                            .size(24.dp)
                            .scale(scale)
                    )
                },
                label = { Text(tab.title, color = iconColor) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = iconColor,
                    unselectedIconColor = iconColor,
                    selectedTextColor = iconColor,
                    unselectedTextColor = iconColor
                )
            )
        }
    }
}