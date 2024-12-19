package com.example.fitquest.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Blue_bell,                // Main interactive color
    onPrimary = Color.White,            // Text/icons on primary color
    secondary = Pacific_cyan,           // Secondary actions
    onSecondary = Color.White,          // Text/icons on secondary color
    tertiary = Non_Photo_blue,          // Accents or highlights
    onTertiary = Color.Black,           // Text/icons on tertiary color
    secondaryContainer = DarkerPacificCyan,  // Darker background for secondary container
    onSecondaryContainer = Color.White,  // Text/icons for secondary container
    tertiaryContainer = Light_cyan,     // Container for tertiary content
    onTertiaryContainer = Color.Black,   // Text/icons for tertiary container

    background = Light_cyan,       // App background color
    onBackground = Federal_blue,   // Text/icons on the background color

    surface = Non_Photo_blue,      // Elevated elements like cards or dialogs
    onSurface = Federal_blue       // Text/icons on surface color
)

@Composable
fun FitQuestTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}