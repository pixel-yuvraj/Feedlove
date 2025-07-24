@file:OptIn(ExperimentalMaterial3Api::class)

package com.feedlove.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.filled.Tune
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.feedlove.app.model.FoodItem
import com.feedlove.app.navigation.Screen
import com.feedlove.app.viewmodel.ProfileViewModel
import com.feedlove.app.viewmodel.ShareFoodViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    rootNavController: NavHostController,
    profileVM: ProfileViewModel = hiltViewModel(),
    shareFoodVM: ShareFoodViewModel = hiltViewModel()
) {
    val user by profileVM.user.collectAsState()
    val username = user?.name?.ifBlank { "User" } ?: "User"
    val foodItems by shareFoodVM.foodItems.collectAsState()

    Column(Modifier.fillMaxSize()) {
        // Greeting Bar
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Hi, $username", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.LocationOn, contentDescription = null, tint = Color.Gray)
                    Spacer(Modifier.width(4.dp))
                    Text("Jaipur", color = Color.Gray)
                    Spacer(Modifier.width(2.dp))
                    Icon(Icons.Filled.ArrowDropDown, contentDescription = null, tint = Color.Gray)
                }
            }
            Row {
                IconButton(onClick = {
                    rootNavController.navigate(Screen.Chat.route) // ✅ Navigate with ROOT controller
                }) {
                    Icon(Icons.Outlined.ChatBubbleOutline, contentDescription = "Chat")
                }


                IconButton(onClick = { /* Sort action */ }) {
                    Icon(Icons.Default.Tune, contentDescription = "Sort")
                }
            }
        }

        // Food List
        Column(
            Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Available Food", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(16.dp))

            for (item in foodItems) {
                FoodCard(item = item, onViewDetails = {
                    rootNavController.navigate("${Screen.BorrowFood.route}/${item.listingId}")

                })
            }

        }
    }
}

@Composable
fun FoodCard(item: FoodItem, onViewDetails: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F6FF))
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = rememberAsyncImagePainter(item.photoUrl),
                contentDescription = item.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(70.dp)
                    .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
            )
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(item.name, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Text(item.description, color = Color.Gray, fontSize = 13.sp)
                Text("by ${item.posterName}", color = Color.DarkGray, fontSize = 12.sp)
            }
            Button(onClick = onViewDetails) {
                Text("View Details")
            }
        }
    }
}
