package com.supplementstore.models

data class ExerciseResponse(
    val status: String,
    val exercise: String,
    val angle: Double,
    val reps_count: Int,
    val hold_timer: Int?,
    val feedback: String
)