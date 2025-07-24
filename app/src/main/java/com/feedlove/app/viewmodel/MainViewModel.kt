package com.feedlove.app.viewmodel

import androidx.lifecycle.ViewModel
import com.feedlove.app.utils.auth

class MainViewModel : ViewModel() {

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }
}
