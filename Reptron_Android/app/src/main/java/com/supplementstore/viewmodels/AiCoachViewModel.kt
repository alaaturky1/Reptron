package com.supplementstore.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supplementstore.models.ExerciseResponse
import com.supplementstore.services.api.PowerFuelApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody


class AiCoachViewModel(private val apiService: PowerFuelApi) : ViewModel() {

    private val _uiState = MutableStateFlow<ExerciseResponse?>(null)
    val uiState = _uiState.asStateFlow()

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing = _isAnalyzing.asStateFlow()

    fun processFrame(imageBytes: ByteArray, exerciseName: String) {
        if (_isAnalyzing.value) return

        viewModelScope.launch {
            _isAnalyzing.value = true
            try {
                val requestFile = imageBytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("file", "frame.jpg", requestFile)
                val exerciseBody = exerciseName.toRequestBody("text/plain".toMediaTypeOrNull())

                val response = apiService.analyzeExercise(exerciseBody, body)
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
}