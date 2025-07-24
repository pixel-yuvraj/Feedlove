// com/feedlove/app/viewmodel/BorrowFoodViewModel.kt
package com.feedlove.app.viewmodel

import androidx.lifecycle.ViewModel
import com.feedlove.app.model.FoodItem
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class BorrowFoodViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _item = MutableStateFlow<FoodItem?>(null)
    val item: StateFlow<FoodItem?> = _item

    fun loadItem(listingId: String) {
        firestore.collection("shared_food")
            .document(listingId)
            .get()
            .addOnSuccessListener { snap ->
                _item.value = snap.toObject(FoodItem::class.java)
            }
            .addOnFailureListener {
                _item.value = null
            }
    }
}
