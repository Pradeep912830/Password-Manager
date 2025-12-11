package com.example.passwordmanager.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.passwordmanager.security.AESCipher
import com.example.passwordmanager.ui.model.PasswordItem

class PasswordDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "password_manager.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE passwords (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                accountName TEXT NOT NULL,
                username TEXT NOT NULL,
                password TEXT NOT NULL
            );
        """.trimIndent()

        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS passwords")
        onCreate(db)
    }


    // Insert data
    fun insertPassword(item: PasswordItem): Boolean {
        return try {
            val db = writableDatabase
            val values = ContentValues().apply {
                put("accountName", item.accountName)
                put("username", item.userName)
                put("password", AESCipher.encrypt(item.password))
            }
            val id = db.insert("passwords", null, values)
            id != -1L
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    // Fetch all accounts
    fun getAllPasswords(): List<PasswordItem> {
        val result = mutableListOf<PasswordItem>()
        val db = readableDatabase
        var cursor = db.rawQuery("SELECT id, accountName, username, password FROM passwords", null)
        try {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                    val accountName = cursor.getString(cursor.getColumnIndexOrThrow("accountName"))
                    val username = cursor.getString(cursor.getColumnIndexOrThrow("username"))
                    val stored = cursor.getString(cursor.getColumnIndexOrThrow("password"))
                    val decrypted = try {
                        AESCipher.decrypt(stored)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        stored // fallback to raw stored
                    }

                    result.add(PasswordItem(id = id, accountName = accountName, userName = username, password = decrypted))
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor.close()
        }
        return result
    }


    // Delete the data
    fun deleteById(id: Int): Boolean {
        return try {
            val db = writableDatabase
            val rows = db.delete("passwords", "id = ?", arrayOf(id.toString()))
            rows > 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    // Update password
    fun updatePasswordById(id: Int, newItem: PasswordItem): Boolean {
        return try {
            val db = writableDatabase
            val values = ContentValues().apply {
                put("accountName", newItem.accountName)
                put("username", newItem.userName)
                put("password", AESCipher.encrypt(newItem.password))
            }
            val rows = db.update("passwords", values, "id = ?", arrayOf(id.toString()))
            rows > 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
