package com.example.fitquest.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import java.io.IOException

@Composable
fun ProfileScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        LazyColumn(modifier = Modifier.widthIn(max = 600.dp)) {
            item { CategoryItem(title = "Account", icon = Icons.Outlined.AccountCircle, onClick = {}) }
            item { CategoryItem(title = "Privacy", icon = Icons.Outlined.Lock, onClick = { }) }
            item { CategoryItem(title = "Notifications", icon = Icons.Outlined.Notifications, onClick = { }) }
            item { HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp)) }
            item { CategoryItem(title = "Send Feedback", icon = Icons.Outlined.Email, onClick = { }) }
            item { HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp)) }
            item { CategoryItem(title = "Log Out", icon = Icons.Outlined.Lock, onClick = { logout() }) }
        }
    }
}

@Composable
fun CategoryItem(title: String, icon: ImageVector, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp), horizontalArrangement = Arrangement.spacedBy(30.dp)) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(28.dp), tint = MaterialTheme.colorScheme.onSurface)
            Text(title, style = MaterialTheme.typography.bodyLarge)
        }
    }
}


fun logout() {
    try {
        // Execute adb wipe-data command to wipe the emulator's data
        val command = "adb shell am broadcast -a android.intent.action.MASTER_CLEAR"
        val process = Runtime.getRuntime().exec(command)
        process.waitFor()

        // Show a toast message when the operation is complete
    } catch (e: IOException) {
        e.printStackTrace()
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }
}
