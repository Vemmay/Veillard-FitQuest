package com.example.fitquest.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fitquest.ExerciseInput
import com.example.fitquest.data.ExerciseType
import java.time.ZonedDateTime
import kotlin.time.Duration.Companion.minutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseInputScreen(
    exerciseInput: ExerciseInput,
    onInsertClick: (ExerciseInput) -> Unit,
) {
    val options: List<ExerciseType> = listOf(
        ExerciseType.Running,
        ExerciseType.Walking,
        ExerciseType.Other
    )

    var expanded by remember { mutableStateOf(false) }
    val userExerciseInput = (remember { mutableStateOf(exerciseInput) }).value

    // Exercise Input UI
    Column(modifier = Modifier.padding(16.dp)) {
        // Title Input (Without Trailing Icon)
        var text by remember { mutableStateOf("") }

        TextField(
            value = text,
            onValueChange = { newText ->
                text = newText; userExerciseInput.exerciseTitle = newText
            },
            label = { Text(text = "Title") }
        )

        // Start Time Picker (Example with current time)
        Text("Start Time:")
        Button(onClick = { userExerciseInput.startTime = ZonedDateTime.now() } // Example action
        ){
            Text("Set Start Time")
        }

        // Duration Picker
        Text("Duration:")
        Button(onClick = { userExerciseInput.duration = 10.minutes })
        {
            Text("Set Duration")
        }

        var dropdownText by remember { mutableStateOf("") }

        // UI for Exercise Type Dropdown
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

        // Insert Button
        Button(
            onClick = {
                onInsertClick(userExerciseInput) // You can change these values to the ones you need
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            // Icon inside the button
            Icon(
                imageVector = Icons.Filled.AddCircle, // This adds the Add icon
                contentDescription = "Add Exercise",
                modifier = Modifier.weight(1f)
            )
        }

    }
}