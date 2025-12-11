package com.example.passwordmanager.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.passwordmanager.viewmodel.PasswordViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewAccountSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    viewModel: PasswordViewModel
) {
    val context = LocalContext.current

    var accountName by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val containerColor = MaterialTheme.colorScheme.surface
    val buttonColor = MaterialTheme.colorScheme.primary

    val accountNamePattern = Regex("^[A-Za-z]{3,30}$")
    val emailPattern = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    val usernamePattern = Regex("^[A-Za-z0-9._]{3,20}$")
    val passwordPattern = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")


    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        containerColor = containerColor,
        shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp),
        tonalElevation = 4.dp
    ) {

        Column(Modifier.padding(20.dp)) {

            AppTextFieldWithValue(
                value = accountName,
                onValueChange = { accountName = it },
                hint = "Account Name"
            )

            AppTextFieldWithValue(
                value = userName,
                onValueChange = { userName = it },
                hint = "Username / Email"
            )

            AppTextFieldWithValue(
                value = password,
                onValueChange = { password = it },
                hint = "Password"
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {


                    if (accountName.isNotEmpty() && userName.isNotEmpty() && password.isNotEmpty()) {

                        // Validations
                        val isAccountNameValid = accountName.matches(accountNamePattern)
                        val isUserValid = userName.matches(emailPattern) || userName.matches(usernamePattern)
                        val isPasswordValid = password.matches(passwordPattern)


                        when {
                            !isAccountNameValid -> {
                                Toast.makeText(context, "Account Name must contain only letters", Toast.LENGTH_SHORT).show()
                            }
                            !isUserValid -> {
                                Toast.makeText(context, "Enter a valid email or username!", Toast.LENGTH_SHORT).show()
                            }
                            !isPasswordValid -> {
                                Toast.makeText(
                                    context,
                                    "Password must be 8+ chars with uppercase, lowercase, number, and special char!",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            else -> {
                                val success = viewModel.addNewAccount(accountName, userName, password)
                                if (success) onDismiss()
                            }
                        }

                    } else {
                        Toast.makeText(context, "All fields are required!", Toast.LENGTH_SHORT).show()
                    }

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Add New Account")
            }
        }
    }
}
