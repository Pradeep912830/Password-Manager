package com.example.passwordmanager.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.passwordmanager.ui.model.PasswordItem
import com.example.passwordmanager.viewmodel.PasswordViewModel
import com.example.passwordmanager.viewmodel.PasswordViewModelFactory

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {

    val context = LocalContext.current
    val viewModel: PasswordViewModel = viewModel(
        factory = PasswordViewModelFactory(context)
    )

    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5
    val backgroundColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDark) Color.White else Color.Black

    val sheetState = rememberModalBottomSheetState()
    var showAddSheet by remember { mutableStateOf(false) }
    var showDetailsSheet by remember { mutableStateOf(false) }

    // List of items
    val passwords = remember { mutableStateListOf<PasswordItem>() }

    LaunchedEffect(Unit) {
        passwords.clear()
        passwords.addAll(viewModel.getAllAccounts())
    }

    var selectedAccount by remember { mutableStateOf<PasswordItem?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddSheet = true },
                containerColor = Color(0xFF2F80ED),
                modifier = Modifier.padding(bottom = 30.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
            }
        }
    ) {

        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(backgroundColor)
        ) {

            Text(
                text = "Password Manager",
                fontSize = 25.sp,
                color = textColor,
                modifier = Modifier.padding(20.dp)
            )

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(passwords.size) { index ->
                    val item = passwords[index]

                    AccountRow(
                        item = item,
                        onClick = {
                            selectedAccount = item
                            showDetailsSheet = true
                        }
                    )
                }
            }
        }
    }

    if (showAddSheet) {
        AddNewAccountSheet(
            sheetState,
            onDismiss = {
                showAddSheet = false
                passwords.clear()
                passwords.addAll(viewModel.getAllAccounts())
            },
            viewModel = viewModel
        )
    }

    if (showDetailsSheet) {
        selectedAccount?.let { item ->
            AccountDetailsSheet(
                sheetState = sheetState,
                item = item,
                onDismiss = {
                    showDetailsSheet = false
                    passwords.clear()
                    passwords.addAll(viewModel.getAllAccounts())
                },
                onDelete = { item ->
                    viewModel.deleteAccount(item)
                    showDetailsSheet = false
                    passwords.clear()
                    passwords.addAll(viewModel.getAllAccounts())
                },
                onUpdate = { oldItem, updatedItem ->
                    val result = viewModel.updateAccount(oldItem, updatedItem)

                    if (result) {
                        passwords.clear()
                        passwords.addAll(viewModel.getAllAccounts())
                    }

                    showDetailsSheet = false
                }

            )
        }
    }

}
