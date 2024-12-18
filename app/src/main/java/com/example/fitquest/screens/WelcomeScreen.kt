package com.example.fitquest.screens

// File citation
/*
 * Title: android-health-connect-codelab
 * Author: meihua
 * Date: 2024
 * Code Version: TODO - add version
 * License: Apache License 2.0
 * Availability: start/src/main/java/com/example/healthconnect/codelab/presentation/screen/WelcomeScreen.kt
 * Modifications: TODO - add modifications
 */

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.fitquest.component.InstalledMessage
import com.example.fitquest.component.NotInstalledMessage
import com.example.fitquest.component.NotSupportedMessage
import com.example.fitquest.data.HealthConnectAvailability
import com.example.fitquest.ui.theme.FitQuestTheme

/**
 * Welcome screen shown when the app is first launched.
 */
@Composable
fun WelcomeScreen(
    healthConnectAvailability: HealthConnectAvailability,
    onResumeAvailabilityCheck: () -> Unit,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
) {
    val currentOnAvailabilityCheck by rememberUpdatedState(onResumeAvailabilityCheck)

    // Add a listener to re-check whether Health Connect has been installed each time the Welcome
    // screen is resumed: This ensures that if the user has been redirected to the Play store and
    // followed the onboarding flow, then when the app is resumed, instead of showing the message
    // to ask the user to install Health Connect, the app recognises that Health Connect is now
    // available and shows the appropriate welcome.
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                currentOnAvailabilityCheck()
            }
        }

        // Add the observer to the lifecycle
        lifecycleOwner.lifecycle.addObserver(observer)

        // When the effect leaves the Composition, remove the observer
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(32.dp))
        when (healthConnectAvailability) {
            HealthConnectAvailability.INSTALLED -> InstalledMessage()
            HealthConnectAvailability.NOT_INSTALLED -> NotInstalledMessage()
            HealthConnectAvailability.NOT_SUPPORTED -> NotSupportedMessage()
        }
    }
}


@Preview
@Composable
fun InstalledMessagePreview() {
    FitQuestTheme {
        WelcomeScreen(
            healthConnectAvailability = HealthConnectAvailability.INSTALLED,
            onResumeAvailabilityCheck = {}
        )
    }
}

@Preview
@Composable
fun NotInstalledMessagePreview() {
    FitQuestTheme {
        WelcomeScreen(
            healthConnectAvailability = HealthConnectAvailability.NOT_INSTALLED,
            onResumeAvailabilityCheck = {}
        )
    }
}

@Preview
@Composable
fun NotSupportedMessagePreview() {
    FitQuestTheme {
        WelcomeScreen(
            healthConnectAvailability = HealthConnectAvailability.NOT_SUPPORTED,
            onResumeAvailabilityCheck = {}
        )
    }
}