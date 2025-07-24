package com.feedlove.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.feedlove.app.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    nestedNavController: NavController,
    rootNavController: NavController,
    onBack: () -> Unit
) {
    var darkModeEnabled by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
    ) {
        TopAppBar(
            title = { Text("Settings", fontSize = 20.sp) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )

        Spacer(Modifier.height(16.dp))

        // --- GENERAL ---
        SectionHeader("General")

        SettingRow(
            modifier = Modifier.padding(horizontal = 16.dp),
            content = {
                Text("Dark Mode", style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.weight(1f))
                Switch(
                    checked = darkModeEnabled,
                    onCheckedChange = { darkModeEnabled = it }
                )
            }
        )
        Spacer(Modifier.height(8.dp))

        SettingRow(
            modifier = Modifier.padding(horizontal = 16.dp),
            onClick = { /* TODO */ },
            content = {
                Text("Language", style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.weight(1f))
                Text("English", color = Color.Gray)
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
            }
        )
        Spacer(Modifier.height(8.dp))

        SettingRow(
            modifier = Modifier.padding(horizontal = 16.dp),
            onClick = { /* TODO */ },
            content = {
                Text("Notifications", style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.weight(1f))
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
            }
        )
        Spacer(Modifier.height(8.dp))

        SettingRow(
            modifier = Modifier.padding(horizontal = 16.dp),
            onClick = { /* TODO */ },
            content = {
                Text("App Settings", style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.weight(1f))
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
            }
        )

        Spacer(Modifier.height(24.dp))

        // --- SUPPORT ---
        SectionHeader("Support")

        SettingRow(
            modifier = Modifier.padding(horizontal = 16.dp),
            onClick = { /* TODO */ },
            content = {
                Text("Help Center", style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.weight(1f))
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
            }
        )

        SettingRow(
            modifier = Modifier.padding(horizontal = 16.dp),
            onClick = {
                rootNavController.navigate(Screen.PrivacyPolicy.route) // ✅ updated safe route
            },
            content = {
                Text("Privacy Policy", style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.weight(1f))
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
            }
        )
        Spacer(Modifier.height(8.dp))

        SettingRow(
            modifier = Modifier.padding(horizontal = 16.dp),
            onClick = { /* TODO */ },
            content = {
                Text("About", style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.weight(1f))
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
            }
        )

        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun SectionHeader(label: String) {
    Text(
        text = label.uppercase(),
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
    )
}

@Composable
private fun SettingRow(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 1.dp,
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            content()
        }
    }
}