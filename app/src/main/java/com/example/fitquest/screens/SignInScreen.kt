package com.example.fitquest.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitquest.SignInViewModel


@Composable
fun SignInScreen(onSignInWithGoogleClick: () -> Unit) {
    val signInViewModel = SignInViewModel()

    Column(verticalArrangement = Arrangement.Bottom,modifier = Modifier
        .padding(horizontal = 26.dp)
        .fillMaxSize()
    ) {
        val gradient = Brush.linearGradient(
            colors = listOf(Color(0xFFFBFCFF), Color(0xFFE5EAFA)),
            start = Offset(0f, 100f),
            end = Offset(40f, 0f)
        )
        Text(
            "Create Your\n" +
                    "Free Account",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontSize = 26.sp,
                lineHeight = 36.sp,
                brush = gradient
            ),
            color = Color(0xFF0A0000),
            modifier = Modifier.padding(14.dp).padding(bottom = 25.dp)
        )

        Button(
            onClick = onSignInWithGoogleClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                disabledContentColor = MaterialTheme.colorScheme.surfaceContainerLow,
                disabledContainerColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        ) {
            Text("Continue With Google")
        }
    }
}
