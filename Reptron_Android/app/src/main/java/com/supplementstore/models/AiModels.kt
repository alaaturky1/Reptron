package com.supplementstore.models

data class ExerciseResponse(
    val status: String,
    val exercise: String,
    val angle: Double,
    val reps_count: Int,
    val hold_timer: Int?,
    val feedback: String
)

data class StartSessionRequest(
    val language: String,
    val level: String
)

data class FrameData(
    val exercise: String,
    val angles: Map<String, Float>,
    val timestamp: Long,
    val frameId: Int,
    val imageB64: String
)

data class AnalyzeFrameRequest(
    val sessionId: String,
    val frame: FrameData
)

data class EndSessionRequest(
    val sessionId: String
)

data class StartSessionResponse(
    val sessionId: String
)