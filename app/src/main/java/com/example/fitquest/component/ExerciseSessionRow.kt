package com.example.fitquest.component
/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.records.ExerciseSessionRecord
import com.example.fitquest.data.ExerciseType
import com.example.fitquest.ui.theme.FitQuestTheme
import java.time.ZonedDateTime
import java.util.UUID

/**
 * Creates a row to represent an [ExerciseSessionRecord]
 */
@Composable
fun ExerciseSessionRow(
    start: ZonedDateTime,
    end: ZonedDateTime,
    uid: String,
    name: String,
    onDetailsClick: (String) -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ExerciseSessionInfoColumn(
            start = start,
            end = end,
            uid = uid,
            name = name,
            type = ExerciseType,
            onClick = onDetailsClick
        )
    }
}

/**
 * Displays summary information about the [ExerciseSessionRecord]
 */
@Composable
fun ExerciseSessionInfoColumn(
    start: ZonedDateTime,
    end: ZonedDateTime,
    uid: String,
    name: String,
    type: ExerciseType.Companion,
    onClick: (String) -> Unit = {},
) {
    Column(
        modifier = Modifier.clickable {
            onClick(uid)
        }
    ) {
        Text(
            color = MaterialTheme.colorScheme.primary,
            text = "${start.toLocalTime().hour%24}:${start.minute} - ${end.toLocalTime().hour}:${end.minute}",
            style = MaterialTheme.typography.labelMedium
        )
        Text(name)
        Spacer(modifier = Modifier.height(4.dp))
        Text(ExerciseType.toExerciseType(name).titleResId.toString())
        //Text(uid)
    }
}

@Preview
@Composable
fun ExerciseSessionRowPreview() {
    FitQuestTheme {
        ExerciseSessionRow(
            ZonedDateTime.now().minusMinutes(30),
            ZonedDateTime.now(),
            UUID.randomUUID().toString(),
            "Running"
        )
    }
}