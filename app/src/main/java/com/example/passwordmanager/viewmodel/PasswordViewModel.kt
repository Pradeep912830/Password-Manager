package com.example.passwordmanager.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.passwordmanager.database.PasswordDatabaseHelper
import com.example.passwordmanager.repository.PasswordRepository
import com.example.passwordmanager.ui.model.PasswordItem

class PasswordViewModel(context: Context) : ViewModel() {

    private val db = PasswordDatabaseHelper(context)
    private val repository = PasswordRepository(db)


    fun addNewAccount(account: String, user: String, pass: String): Boolean {
        if (account.isBlank() || user.isBlank() || pass.isBlank()) return false
        val item = PasswordItem(accountName = account.trim(), userName = user.trim(), password = pass)
        return repository.addAccount(item)
    }


    fun getAllAccounts(): List<PasswordItem> {
        return repository.getAllAccounts()
    }

    fun deleteAccount(item: PasswordItem): Boolean {
        val id = item.id ?: return false
        return repository.deleteAccountById(id)
    }


    fun updateAccount(oldItem: PasswordItem, newItem: PasswordItem): Boolean {
        val id = oldItem.id ?: return false
        if (newItem.accountName.isBlank() || newItem.userName.isBlank() || newItem.password.isBlank()) return false
        return repository.updateAccountById(id, newItem.copy(password = newItem.password))
    }
}
