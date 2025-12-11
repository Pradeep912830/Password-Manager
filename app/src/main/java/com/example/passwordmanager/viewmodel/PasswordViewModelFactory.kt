package com.example.passwordmanager.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PasswordViewModelFactory(private val context: Context) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PasswordViewModel::class.java)) {
            return PasswordViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
