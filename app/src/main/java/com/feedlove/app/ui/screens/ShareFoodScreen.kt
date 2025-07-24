// com/feedlove/app/ui/screens/ShareFoodScreen.kt
package com.feedlove.app.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.feedlove.app.utils.ImageUtils // ← NEW
import com.feedlove.app.viewmodel.ShareFoodViewModel
import kotlinx.coroutines.launch

@Composable
fun ShareFoodScreen(
    navController: NavController,
    viewModel: ShareFoodViewModel
) {
    var foodName         by remember { mutableStateOf("") }
    var description      by remember { mutableStateOf("") }
    var quantity         by remember { mutableStateOf("") }
    var imageUri         by remember { mutableStateOf<Uri?>(null) }
    var selectedFoodType by remember { mutableStateOf("Vegetarian") }
    var isUploading      by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(GetContent()) {
        imageUri = it
    }

    val scope             = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val ctx               = LocalContext.current                   // ← NEW: for compression

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "List Food Item",
                style = MaterialTheme.typography.headlineSmall.copy(color = Color.Black),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (imageUri == null) {
                    TextButton(onClick = { launcher.launch("image/*") }) {
                        Text("Upload a food image", fontSize = 14.sp)
                    }
                } else {
                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = "Food Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    )
                }
            }

            OutlinedTextField(
                value = foodName,
                onValueChange = { foodName = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = quantity,
                onValueChange = { quantity = it },
                label = { Text("Quantity") },
                modifier = Modifier.fillMaxWidth()
            )

            FoodTypeDropdown(
                selectedFoodType = selectedFoodType,
                onSelectedChange = { selectedFoodType = it }
            )

            OutlinedTextField(
                value = "Jaipur, Rajasthan",
                onValueChange = {},
                label = { Text("Pickup Location") },
                modifier = Modifier.fillMaxWidth(),
                enabled = false
            )

            Button(
                onClick = {
                    scope.launch {
                        if (foodName.isBlank() || description.isBlank() ||
                            quantity.isBlank() || imageUri == null
                        ) {
                            snackbarHostState.showSnackbar("Fill all fields & upload an image.")
                            return@launch
                        }

                        isUploading = true

                        // ← NEW: compress the selected URI
                        val bytes = ImageUtils.compressAndResize(
                            context   = ctx,
                            uri       = imageUri!!,
                            maxWidth  = 800,
                            maxHeight = 800,
                            quality   = 75
                        )


                        // ← NEW: call the new byte‑based uploader
                        viewModel.uploadFoodItemBytes(
                            imageBytes     = bytes,
                            title          = foodName,
                            description    = description,
                            quantity       = quantity,
                            foodType       = selectedFoodType,
                            pickupLocation = "Jaipur, Rajasthan",
                            onSuccess = {
                                isUploading = false
                                navController.popBackStack()
                            },
                            onFailure = { err ->
                                isUploading = false
                                scope.launch { snackbarHostState.showSnackbar("Upload failed: $err") }
                            }
                        )
                    }
                },
                enabled = !isUploading,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                if (isUploading) {
                    CircularProgressIndicator(
                        color       = Color.White,
                        strokeWidth = 2.dp,
                        modifier    = Modifier.size(20.dp)
                    )
                } else {
                    Text("List This Food", color = Color.White)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodTypeDropdown(
    selectedFoodType: String,
    onSelectedChange: (String) -> Unit
) {
    val foodTypes = listOf("Vegetarian", "Non-Vegetarian", "Vegan", "Packaged", "Cooked")
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value        = selectedFoodType,
            onValueChange= {},
            readOnly     = true,
            label        = { Text("Food Type") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier     = Modifier.fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            foodTypes.forEach { option ->
                DropdownMenuItem(
                    text    = { Text(option) },
                    onClick = {
                        onSelectedChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
