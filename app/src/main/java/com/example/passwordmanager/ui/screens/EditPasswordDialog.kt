package com.example.passwordmanager.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.passwordmanager.ui.model.PasswordItem

@Composable
fun EditPasswordDialog(
    item: PasswordItem,
    onSave: (PasswordItem) -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    var account by remember { mutableStateOf(item.accountName) }
    var username by remember { mutableStateOf(item.userName) }
    var password by remember { mutableStateOf(item.password) }

    val accountNamePattern = Regex("^[A-Za-z]{3,30}$")
    val emailPattern = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    val usernamePattern = Regex("^[A-Za-z0-9._]{3,20}$")
    val passwordPattern = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")

    AlertDialog(
        onDismissRequest = { onCancel() },
        confirmButton = {
            Button(onClick = {

                if(!account.matches(accountNamePattern)) {
                   Toast.makeText(context, "Account Name must contain only letters", Toast.LENGTH_SHORT).show()

                }else if(!(username.matches(emailPattern) || username.matches(usernamePattern))){
                    Toast.makeText(context, "Enter a valid email or username!", Toast.LENGTH_SHORT).show()

                }else if(!password.matches(passwordPattern)){
                    Toast.makeText(
                        context,
                        "Password must be 8+ chars with uppercase, lowercase, number, and special char!",
                        Toast.LENGTH_LONG
                    ).show()

                }else{

                    onSave(
                        PasswordItem(
                            accountName = account,
                            userName = username,
                            password = password
                        )
                    )

                }

            }, shape = RoundedCornerShape(4.dp)) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onCancel, shape = RoundedCornerShape(4.dp)) {
                Text("Cancel")
            }
        },
        title = { Text("Edit Password") },
        text = {
            Column {

                OutlinedTextField(
                    value = account,
                    onValueChange = { account = it },
                    label = { Text("Account") }
                )

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username / Email") }
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") }
                )
            }
        },
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.padding(16.dp)
    )
}
