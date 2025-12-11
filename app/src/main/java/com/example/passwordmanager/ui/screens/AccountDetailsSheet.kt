package com.example.passwordmanager.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.passwordmanager.ui.model.PasswordItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailsSheet(
    sheetState: SheetState,
    item: PasswordItem,
    onDismiss: () -> Unit,
    onDelete: (PasswordItem) -> Unit,
    onUpdate: (PasswordItem, PasswordItem) -> Unit
) {

    var passwordVisible by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    val containerColor = MaterialTheme.colorScheme.surface
    val labelColor = MaterialTheme.colorScheme.onSurfaceVariant
    val textColor = MaterialTheme.colorScheme.onSurface

    val editButtonColor = MaterialTheme.colorScheme.primary
    val deleteButtonColor = MaterialTheme.colorScheme.error


    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Account?") },
            text = { Text("Are you sure you want to delete this saved password?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete(item)
                        showDeleteDialog = false
                        onDismiss()
                    },
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = deleteButtonColor)
                ) {
                    Text("Yes, Delete")
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = false }, shape = RoundedCornerShape(5.dp)) {
                    Text("Cancel")
                }
            },
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(10.dp)
        )
    }


    if (showEditDialog) {
        EditPasswordDialog(
            item = item,
            onSave = { newItem ->
                onUpdate(item, newItem)
                showEditDialog = false
                onDismiss()
            },
            onCancel = { showEditDialog = false }
        )
    }


    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        containerColor = containerColor,
        shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp)
    ) {

        Column(Modifier.padding(16.dp)) {

            Text(
                "Account Details",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )

            Spacer(Modifier.height(15.dp))

            Text("Account Type", fontSize = 14.sp, color = labelColor)
            Text(item.accountName, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)

            Spacer(Modifier.height(15.dp))

            Text("Username / Email", fontSize = 14.sp, color = labelColor)
            Text(item.userName, fontSize = 18.sp)

            Spacer(Modifier.height(15.dp))

            Text("Password", fontSize = 14.sp, color = labelColor)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (passwordVisible) item.password else "•".repeat(item.password.length),
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.VisibilityOff
                        else Icons.Default.Visibility,
                        contentDescription = null
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                Button(
                    onClick = { showEditDialog = true },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Edit", fontSize = 15.sp)
                }

                Spacer(Modifier.width(15.dp))

                Button(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = deleteButtonColor)
                ) {
                    Text("Delete", fontSize = 15.sp)
                }
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}
