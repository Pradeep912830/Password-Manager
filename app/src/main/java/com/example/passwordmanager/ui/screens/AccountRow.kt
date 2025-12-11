package com.example.passwordmanager.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.passwordmanager.ui.model.PasswordItem

@Composable
fun AccountRow(item: PasswordItem, onClick: () -> Unit) {

    // Detect dark mode
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5

    val backgroundColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDark) Color.White else Color.Black
    val maskedColor = if (isDark) Color.LightGray else Color.Gray

    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .fillMaxWidth()
            .background(backgroundColor, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.accountName,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = textColor
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = "*****",
            fontSize = 20.sp,
            color = maskedColor,
            modifier = Modifier.weight(1f)
        )

        Icon(
            Icons.Outlined.ArrowForwardIos,
            contentDescription = "Arrow",
            tint = textColor,
            modifier = Modifier.size(18.dp)
        )
    }
}
