@file:OptIn(ExperimentalMaterial3Api::class)

package com.feedlove.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.feedlove.app.model.FoodItem
import com.feedlove.app.viewmodel.ShareFoodViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.navigation.NavHostController
import coil.compose.AsyncImage


@Composable
fun BorrowFoodScreen(
    navController: NavHostController,
    navBackStackEntry: androidx.navigation.NavBackStackEntry,
    viewModel: ShareFoodViewModel
) {
    val listingId = navBackStackEntry.arguments?.getString("listingId") ?: return
    var foodItem by remember { mutableStateOf<FoodItem?>(null) }
    var posterImageUrl by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Fetch food details
    LaunchedEffect(listingId) {
        viewModel.fetchFoodItemById(
            listingId,
            onSuccess = {
                foodItem = it
                // Fetch poster image
                FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(it.donorId)
                    .get()
                    .addOnSuccessListener { snap ->
                        posterImageUrl = snap.getString("profilePicUrl")
                    }
            },
            onFailure = {
                scope.launch {
                    snackbarHostState.showSnackbar("Food item not found.")
                }
            }
        )
    }

    foodItem?.let { food ->
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = { Text("Borrow Food") },
                    navigationIcon = {
                        IconButton(onClick = {
                            navController.popBackStack()
                        }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            },
            bottomBar = {
                Box(Modifier.padding(16.dp)) {
                    Button(
                        onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar("Request sent to ${food.posterName}")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF66103))
                    ) {
                        Text("Request Food", color = Color.White)
                    }
                }
            }
        ) { inner ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(inner)
                    .padding(bottom = 80.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Image(
                    painter = rememberAsyncImagePainter(food.photoUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    contentScale = ContentScale.Crop
                )

                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        text = food.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1B130D)
                    )

                    Text(food.description, fontSize = 16.sp, color = Color.Gray)

                    Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                        Column {
                            Text("Food Type", fontWeight = FontWeight.Bold, color = Color(0xFF1B130D))
                            Text(food.foodType, color = Color.Gray)
                        }
                        Column {
                            Text("Quantity", fontWeight = FontWeight.Bold, color = Color(0xFF1B130D))
                            Text(food.quantity, color = Color.Gray)
                        }
                    }

                    Divider()

                    Text("Lender", fontWeight = FontWeight.Bold, fontSize = 18.sp)

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        if (!posterImageUrl.isNullOrBlank()) {
                            AsyncImage(
                                model = posterImageUrl,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(Color.LightGray)
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(Color.LightGray),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Default profile",
                                    tint = Color.DarkGray,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }

                        Spacer(Modifier.width(12.dp))

                        Column {
                            Text(food.posterName, fontWeight = FontWeight.Medium)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = Color.Gray,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text("Jaipur", color = Color.Gray, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
        }
    } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}
