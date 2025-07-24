// com/feedlove/app/ui/screens/ChatScreen.kt
@file:OptIn(ExperimentalMaterial3Api::class)

package com.feedlove.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.ArrowBack
import androidx.navigation.NavController


data class ChatUser(
    val name: String,
    val message: String,
    val timestamp: String,
    val unreadCount: Int = 0,
    val isOnline: Boolean = false
)

private val dummyChats = listOf(
    ChatUser("Sarah", "Hi, I have some extra bread...", "10:30 AM", 2, true),
    ChatUser("Mark",  "I’m looking for some fresh produce...", "Yesterday", 0, false),
    ChatUser("Emily", "I have some canned goods to donate...", "Mon", 1, true),
    ChatUser("David", "I’m interested in the apples you posted...", "Oct 28", 0, false),
    ChatUser("Jessica", "I have some extra milk to share...", "Oct 25", 0, false),
    ChatUser("Michael", "I’m looking for some pasta...", "Oct 22", 0, false),
    ChatUser("Olivia", "I have some extra eggs to donate...", "Oct 20", 5, true)
)

@Composable
fun ChatScreen(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Chat", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* dummy search */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
            )
        }
    ) { inner ->
        LazyColumn(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(dummyChats) { user ->
                ChatListItem(user)
            }
        }
    }
}


@Composable
private fun ChatListItem(user: ChatUser) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar placeholder
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
            if (user.isOnline) {
                Box(
                    Modifier
                        .size(10.dp)
                        .background(Color.Green, CircleShape)
                        .align(Alignment.BottomEnd)
                        .offset(x = 2.dp, y = 2.dp)
                )
            }
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(user.name, fontWeight = FontWeight.SemiBold)
            Text(
                user.message,
                fontSize = 14.sp,
                color = Color.Gray,
                maxLines = 1
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(user.timestamp, fontSize = 12.sp, color = Color(0xFF607D8B))
            if (user.unreadCount > 0) {
                Box(
                    Modifier
                        .padding(top = 4.dp)
                        .background(Color(0xFF007AFF), CircleShape)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text("${user.unreadCount}", color = Color.White, fontSize = 12.sp)
                }
            }
        }
    }
}