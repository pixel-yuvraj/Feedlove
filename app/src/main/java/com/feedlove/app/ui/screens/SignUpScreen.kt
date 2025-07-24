package com.feedlove.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.feedlove.app.navigation.Screen
import com.feedlove.app.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: AuthViewModel
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope    = rememberCoroutineScope()

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
        ) {
            // Top curved header
            Surface(
                color   = MaterialTheme.colorScheme.primary,
                shape   = RoundedCornerShape(bottomEnd = 150.dp),
                modifier= Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                Box(Modifier.fillMaxSize()) {
                    Text(
                        text = "Create\nAccount",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontSize = 32.sp,
                            color    = Color.White
                        ),
                        modifier = Modifier
                            .padding(24.dp)
                            .align(Alignment.TopStart)
                    )
                }
            }

            // Form fields
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .align(Alignment.Center)
            ) {
                Spacer(Modifier.height(200.dp))

                OutlinedTextField(
                    value        = name,
                    onValueChange= { name = it },
                    label        = { Text("Full Name") },
                    modifier     = Modifier.fillMaxWidth(),
                    enabled      = !isLoading
                )
                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value        = email,
                    onValueChange= { email = it },
                    label        = { Text("Email") },
                    modifier     = Modifier.fillMaxWidth(),
                    enabled      = !isLoading
                )
                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value               = password,
                    onValueChange       = { password = it },
                    label               = { Text("Password") },
                    visualTransformation= PasswordVisualTransformation(),
                    modifier            = Modifier.fillMaxWidth(),
                    enabled             = !isLoading
                )
                Spacer(Modifier.height(24.dp))

                Row(
                    verticalAlignment   = Alignment.CenterVertically,
                    horizontalArrangement= Arrangement.SpaceBetween,
                    modifier            = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text     = "Sign Up",
                        fontSize = 18.sp,
                        color    = MaterialTheme.colorScheme.onBackground
                    )

                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onBackground)
                            .clickable(enabled = !isLoading) {
                                coroutineScope.launch {
                                    if (name.isBlank() || email.isBlank() || password.isBlank()) {
                                        snackbarHostState.showSnackbar("All fields are required")
                                    } else {
                                        isLoading = true
                                        viewModel.signUp(
                                            email, password, name,
                                            onSuccess = {
                                                isLoading = false
                                                navController.navigate(Screen.Home.route) {
                                                    popUpTo(Screen.Login.route) { inclusive = true }
                                                }
                                            },
                                            onFailure = { err ->
                                                isLoading = false
                                                coroutineScope.launch {
                                                    snackbarHostState.showSnackbar(err)
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector   = Icons.Filled.ArrowForward,
                            contentDescription = "Sign Up",
                            tint          = Color.White
                        )
                    }
                }
            }

            // "Sign In" link
            Text(
                text = "Sign In",
                color= MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp)
                    .clickable(enabled = !isLoading) {
                        navController.navigate(Screen.Login.route)
                    }
            )

            // Loading overlay
            if (isLoading) {
                Box(
                    modifier        = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment= Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}
