package com.example.fitquest.data

// File citation
/*
 * Title: android-health-connect-codelab
 * Author: meihua
 * Date: 2024
 * Code Version: TODO - add version
 * License: Apache License 2.0
 * Availability: start/src/main/java/com/example/healthconnect/codelab/data/ExerciseSessionData.kt
 * Modifications: TODO - add modifications
 */

import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.SpeedRecord
import androidx.health.connect.client.units.Energy
import androidx.health.connect.client.units.Length
import androidx.health.connect.client.units.Velocity
import java.time.Duration

/**
 * Represents data, both aggregated and raw, associated with a single exercise session. Used to
 * collate results from aggregate and raw reads from Health Connect in one object.
 */
data class ExerciseSessionData(
    val uid: String,
    val totalActiveTime: Duration? = null,
    val totalSteps: Long? = null,
    val totalDistance: Length? = null,
    val totalEnergyBurned: Energy? = null,
    val minHeartRate: Long? = null,
    val maxHeartRate: Long? = null,
    val avgHeartRate: Long? = null,
    val heartRateSeries: List<HeartRateRecord> = listOf(),
    val minSpeed: Velocity? = null,
    val maxSpeed: Velocity? = null,
    val avgSpeed: Velocity? = null,
    val speedRecord: List<SpeedRecord> = listOf(),
)