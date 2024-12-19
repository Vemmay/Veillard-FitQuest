package com.example.fitquest.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeInputDialog(
    onConfirm: (TimePickerState) -> Unit,
    onDismiss: () -> Unit,
) {
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )

    Column {
        TimeInput(
            state = timePickerState,
        )
        Row {
            Button(onClick = { onConfirm(timePickerState) }) {
                Text("Ok")
            }
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowTimePicker(
    onTimeSelected: (Calendar) -> Unit,
    onDismiss: () -> Unit
) {
    var showTimePicker by remember { mutableStateOf(true) }
    rememberTimePickerState()

    if (showTimePicker) {
        TimeInputDialog(
            onDismiss = {
                showTimePicker = false
                onDismiss()
            },
            onConfirm = { timePickerState ->
                val cal = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                    set(Calendar.MINUTE, timePickerState.minute)
                }
                showTimePicker = false
                onTimeSelected(cal)
            },
        )
    }
}

fun Calendar.toZonedDateTime(): ZonedDateTime {
    return ZonedDateTime.ofInstant(this.toInstant(), ZoneId.systemDefault())
}

