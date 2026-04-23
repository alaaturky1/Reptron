package com.supplementstore.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supplementstore.models.AnalyzeFramePayload
import com.supplementstore.models.AnalyzeFrameRequest
import com.supplementstore.models.ExerciseResponse
import com.supplementstore.models.StartSessionRequest
import com.supplementstore.services.api.PowerFuelApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Base64


class AiCoachViewModel(private val apiService: PowerFuelApi) : ViewModel() {

    private val _uiState = MutableStateFlow<ExerciseResponse?>(null)
    val uiState = _uiState.asStateFlow()

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing = _isAnalyzing.asStateFlow()
    private var sessionId: String? = null

    fun processFrame(imageBytes: ByteArray, exerciseName: String) {
        if (_isAnalyzing.value) return

        viewModelScope.launch {
            _isAnalyzing.value = true
            try {
                val sid = ensureSession() ?: run {
                    _isAnalyzing.value = false
                    return@launch
                }
                val b64 = Base64.encodeToString(imageBytes, Base64.NO_WRAP)
                val request = AnalyzeFrameRequest(
                    sessionId = sid,
                    frame = AnalyzeFramePayload(
                        exercise = exerciseName.lowercase(),
                        timestamp = System.currentTimeMillis().toDouble() / 1000.0,
                        imageB64 = b64
                    )
                )

                val response = apiService.analyzeExercise(request)
                if (response.isSuccessful) {
                    _uiState.value = response.body()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isAnalyzing.value = false
            }
        }
    }

    private suspend fun ensureSession(): String? {
        if (!sessionId.isNullOrEmpty()) return sessionId
        val res = apiService.startFitnessSession(StartSessionRequest())
        if (!res.isSuccessful) return null
        val sid = res.body()?.resolvedSessionId ?: return null
        sessionId = sid
        return sid
    }
}