package com.example.fitquest.data

import androidx.health.connect.client.records.ExerciseSessionRecord

data class Exercise(val type: Int, val imageUrl: String)

object ExerciseImages {
    private val exercises = listOf(
        Exercise(ExerciseSessionRecord.EXERCISE_TYPE_RUNNING, "https://hips.hearstapps.com/hmg-prod/images/running-is-one-of-the-best-ways-to-stay-fit-royalty-free-image-1036780592-1553033495.jpg?crop=0.88976xw:1xh;center,top&resize=1200:*"),
        Exercise(ExerciseSessionRecord.EXERCISE_TYPE_WALKING, "https://images.everydayhealth.com/images/healthy-living/fitness/walking-workouts-myths-and-facts-1440x810.jpg"),
        Exercise(ExerciseSessionRecord.EXERCISE_TYPE_OTHER_WORKOUT, "https://example.com/images/other.png"),
        Exercise(ExerciseSessionRecord.EXERCISE_TYPE_YOGA, "https://example.com/images/other.png"),
        Exercise(ExerciseSessionRecord.EXERCISE_TYPE_BIKING, "https://example.com/images/other.png")
    )

    // Function to get the image URL for a given exercise type
    fun getImageUrl(exerciseType: Int): String? {
        return exercises.find { it.type == exerciseType }?.imageUrl
    }
}
