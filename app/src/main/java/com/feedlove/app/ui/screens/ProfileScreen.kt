@file:OptIn(ExperimentalMaterial3Api::class)

package com.feedlove.app.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.feedlove.app.navigation.Screen
import androidx.navigation.NavController
import com.feedlove.app.navigation.MainTab
import com.feedlove.app.viewmodel.ProfileViewModel
import androidx.compose.ui.platform.LocalContext
import com.feedlove.app.utils.ImageUtils
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch



@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel,
    onSignedOut: () -> Unit
) {
    val user by viewModel.user.collectAsState()
    val profilePicUrl by viewModel.profilePicUrl.collectAsState()
    var localImageUri by remember { mutableStateOf<Uri?>(null) }

    val ctx = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(GetContent()) { uri ->
        uri?.let {
            // compress & resize
            val bytes = ImageUtils.compressAndResize(
                context   = ctx,
                uri       = it,
                maxWidth  = 800,
                maxHeight = 800,
                quality   = 75
            )
            viewModel.uploadProfileImageBytes(
                imageBytes = bytes,
                onFailure = { err ->
                    scope.launch { snackbarHostState.showSnackbar(err) }
                }
            )
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
    ) {
        CenterAlignedTopAppBar(
            title = { Text("Profile", fontSize = 20.sp) },
            navigationIcon = {
                IconButton(onClick = {
                    navController.navigate(MainTab.Home.route) {
                        popUpTo(MainTab.Home.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = {
                    navController.navigate(Screen.Settings.route)
                }) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings")
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )

        Spacer(Modifier.height(24.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = CircleShape,
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .size(120.dp)
                    .shadow(4.dp, CircleShape)
                    .clickable { launcher.launch("image/*") }
            ) {
                when {
                    localImageUri != null -> {
                        AsyncImage(
                            model = localImageUri,
                            contentDescription = "Profile photo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize().clip(CircleShape)
                        )
                    }
                    !profilePicUrl.isNullOrBlank() -> {
                        AsyncImage(
                            model = profilePicUrl,
                            contentDescription = "Profile photo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize().clip(CircleShape)
                        )
                    }
                    else -> {
                        Box(
                            Modifier.fillMaxSize().clip(CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Default profile",
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                modifier = Modifier.size(64.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = user?.name.orEmpty(),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray)
            Spacer(Modifier.width(4.dp))
            Text("Jaipur", color = Color.Gray)
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { launcher.launch("image/*") },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(48.dp)
        ) {
            Icon(Icons.Default.Edit, contentDescription = "Edit")
            Spacer(Modifier.width(8.dp))
            Text("Edit Profile")
        }

        Spacer(Modifier.height(32.dp))
        AccountSection()
        SupportSection()
        Spacer(Modifier.weight(1f))

        Button(
            onClick = {
                viewModel.signOut()
                onSignedOut()
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .height(48.dp)
        ) {
            Icon(Icons.Default.ExitToApp, contentDescription = "Logout", tint = Color.White)
            Spacer(Modifier.width(8.dp))
            Text("Logout", color = Color.White)
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable private fun AccountSection() {
    SectionHeader("Account")
    RowItem(Icons.Default.Notifications, "Notifications")
    RowItem(Icons.Default.Lock, "Privacy & Security")
    RowItem(Icons.Default.CreditCard, "Payment Methods")
}

@Composable private fun SupportSection() {
    SectionHeader("Support")
    RowItem(Icons.Default.Help, "Help & Support")
    RowItem(Icons.Default.Info, "About Us")
}

@Composable private fun SectionHeader(title: String) {
    Text(
        title,
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier.padding(start = 24.dp, bottom = 4.dp)
    )
}

@Composable private fun RowItem(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.width(16.dp))
        Text(text, modifier = Modifier.weight(1f))
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
    }
}