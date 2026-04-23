package com.supplementstore.models

import com.google.gson.annotations.SerializedName

data class StartSessionRequest(
    val language: String = "en",
    val level: String = "beginner"
)

data class StartSessionResponse(
    @SerializedName("session_id") val sessionIdSnake: String? = null,
    @SerializedName("sessionId") val sessionIdCamel: String? = null,
    val id: String? = null,
    @SerializedName("ws_url") val wsUrl: String? = null
) {
    val resolvedSessionId: String?
        get() = sessionIdSnake ?: sessionIdCamel ?: id
}

data class AnalyzeFrameRequest(
    @SerializedName("session_id") val sessionId: String,
    val frame: AnalyzeFramePayload
)

data class AnalyzeFramePayload(
    val exercise: String,
    val timestamp: Double,
    @SerializedName("image_b64") val imageB64: String
)

data class ExerciseResponse(
    @SerializedName("rep_count") val repCount: Int? = null,
    @SerializedName("repCount") val repCountCamel: Int? = null,
    val reps: Int? = null,
    val feedback: String? = null,
    val issues: List<String>? = null,
    val paused: Boolean? = null
) {
    val normalizedReps: Int
        get() = repCount ?: repCountCamel ?: reps ?: 0

    val normalizedFeedback: String
        get() = feedback?.takeIf { it.isNotBlank() } ?: "Analyzing..."

    val normalizedStatus: String
        get() = if (paused == true) "paused" else "ok"
}