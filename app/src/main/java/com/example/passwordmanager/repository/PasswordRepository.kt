package com.example.passwordmanager.repository

import com.example.passwordmanager.database.PasswordDatabaseHelper
import com.example.passwordmanager.ui.model.PasswordItem

class PasswordRepository(private val db: PasswordDatabaseHelper) {


    fun addAccount(item: PasswordItem): Boolean {
        return try {
            db.insertPassword(item)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    fun getAllAccounts(): List<PasswordItem> {
        return try {
            db.getAllPasswords()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }


    fun deleteAccountById(id: Int): Boolean {
        return try {
            db.deleteById(id)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    fun updateAccountById(id: Int, newItem: PasswordItem): Boolean {
        return try {
            db.updatePasswordById(id, newItem)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}