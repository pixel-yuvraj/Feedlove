package com.feedlove.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.feedlove.app.model.FoodItem

@Composable
fun FoodListingItem(foodItem: FoodItem) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Load image from photoUrl
            if (foodItem.photoUrl.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(foodItem.photoUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(text = foodItem.name, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = foodItem.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Quantity: ${foodItem.quantity}", style = MaterialTheme.typography.bodySmall)

            if (!foodItem.isFree) {
                Text(text = "Price: ₹${foodItem.price ?: 0.0}", style = MaterialTheme.typography.bodySmall)
            } else {
                Text(text = "Free", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
            }

            // Optional tags
            Row(modifier = Modifier.padding(top = 6.dp)) {
                if (foodItem.vegetarian) Text("🌱 Veg", modifier = Modifier.padding(end = 8.dp))
                if (foodItem.cooked) Text("🔥 Cooked", modifier = Modifier.padding(end = 8.dp))
                if (foodItem.packaged) Text("📦 Packaged")
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "👍 ${foodItem.thumbsUp}", style = MaterialTheme.typography.labelSmall)
        }
    }
}
