package com.example.fitquest.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.fitquest.ExerciseInput
import com.example.fitquest.component.compose_icons.Calendar_clock
import com.example.fitquest.data.ExerciseType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseInputScreen(
    exerciseInput: ExerciseInput,
    onInsertClick: (ExerciseInput) -> Unit,
    onDismiss: () -> Unit
) {
    val options: List<ExerciseType> = listOf(
        ExerciseType.Running,
        ExerciseType.Walking,
        ExerciseType.Other
    )

    var expanded by remember { mutableStateOf(false) }
    val userExerciseInput = (remember { mutableStateOf(exerciseInput) }).value
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }
    //var validInput by remember { mutableStateOf(false) }

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        // Exercise Input UI
        Column(modifier = Modifier.padding(16.dp)) {

            // Section to add title of exercise
            var text by remember { mutableStateOf("") }
            TextField(
                value = text,
                onValueChange = { newText ->
                    text = newText; userExerciseInput.exerciseTitle = newText
                },
                label = { Text(text = "Title") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row (horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(onClick = { showStartTimePicker = true }
                ){
                    Icon(Calendar_clock, contentDescription = "Timer")
                    // Start Time Picker
                    Text("Start")
                }

                if (showStartTimePicker) {
                    ShowTimePicker(
                        onTimeSelected = { selectedTime ->
                            userExerciseInput.startTime = selectedTime.toZonedDateTime()
                        },
                        onDismiss = { showStartTimePicker = false }
                    )
                }

                Spacer(modifier = Modifier.width(72.dp))

                Button(onClick = { showEndTimePicker = true }
                ) {
                    Icon(Calendar_clock, contentDescription = "Timer")
                    // End Time Picker
                    Text("End")
                }

                if (showEndTimePicker) {
                    ShowTimePicker(
                        onTimeSelected = { selectedTime ->
                            userExerciseInput.endTime = selectedTime.toZonedDateTime()
                        },
                        onDismiss = { showEndTimePicker = false }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // UI for Exercise Type Dropdown
            var dropdownText by remember { mutableStateOf("") }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it },
            ) {
                TextField(
                    value = dropdownText,
                    onValueChange = { newText -> dropdownText = newText },
                    modifier = Modifier.menuAnchor(),
                    readOnly = true,
                    label = { Text("Type") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.toString()) },
                            onClick = {
                                dropdownText = option.toString() // Update the text in the TextField
                                userExerciseInput.type = option // Update the selected exercise type
                                expanded = false // Close the dropdown
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row (horizontalArrangement = Arrangement.SpaceEvenly) {
                // Insert Button
                Button(
                    onClick = {
                        onInsertClick(userExerciseInput)
                    },
                    modifier = Modifier.weight(0.5f)
                ) {
                    // Icon inside the button
                    Icon(
                        imageVector = Icons.Filled.AddCircle,
                        contentDescription = "Add Exercise",
                    )
                }
                Spacer(modifier = Modifier.width(120.dp))

                // Cancel Button
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.weight(0.5f)
                ) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = "Cancel")
                }
            }
        }
    }
}

private fun ExerciseInput.isValid(): Boolean {
    return exerciseTitle.isNotEmpty()
}