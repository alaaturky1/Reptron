package com.supplementstore.viewmodels

import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supplementstore.models.AnalyzeFrameRequest
import com.supplementstore.models.EndSessionRequest
import com.supplementstore.models.ExerciseResponse
import com.supplementstore.models.FrameData
import com.supplementstore.models.StartSessionRequest
import com.supplementstore.services.api.PowerFuelApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class AiCoachViewModel(private val apiService: PowerFuelApi) : ViewModel() {

    private val _uiState = MutableStateFlow<ExerciseResponse?>(null)
    val uiState = _uiState.asStateFlow()

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing = _isAnalyzing.asStateFlow()

    // إدارة الجلسة والفريمات
    private var currentSessionId: String? = null
    private var frameCounter = 0

    // 1. بدء الجلسة
    fun startSession(language: String = "en", level: String = "beginner") {
        viewModelScope.launch {
            try {
                frameCounter = 0 // بنصفر العداد بس
                val request = StartSessionRequest(language, level)
                val response = apiService.startFitnessSession(request)

                if (response.isSuccessful && response.body() != null) {
                    currentSessionId = response.body()?.sessionId
                } else {
                    println("Failed to start session: ${response.code()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }    // حفظ اختيارات اليوزر
    private val _selectedLanguage = MutableStateFlow("EN")
    val selectedLanguage = _selectedLanguage.asStateFlow()

    private val _selectedLevel = MutableStateFlow("Beginner")
    val selectedLevel = _selectedLevel.asStateFlow()

    private val _selectedExercise = MutableStateFlow("Squat")
    val selectedExercise = _selectedExercise.asStateFlow()

    // دوال لتحديث الاختيارات
    fun updateLanguage(lang: String) { _selectedLanguage.value = lang }
    fun updateLevel(level: String) { _selectedLevel.value = level }
    fun updateExercise(exercise: String) { _selectedExercise.value = exercise }
    // 2. تحليل الفريم
    fun processFrame(imageBytes: ByteArray, exerciseName: String) {
        // لو مفيش Session شغال أو في فريم بيتحلل حالياً، مانعملش حاجة
        if (_isAnalyzing.value || currentSessionId == null) return

        viewModelScope.launch {
            _isAnalyzing.value = true
            try {
                // تحويل الصورة لـ Base64 بدون فواصل (NO_WRAP مهم جداً عشان الـ JSON)
                val base64Image = Base64.encodeToString(imageBytes, Base64.NO_WRAP)

                val frameData = FrameData(
                    exercise = exerciseName,
                    angles = emptyMap(), // بنبعتها فاضية لأن التطبيق مبيحسبهاش
                    timestamp = System.currentTimeMillis(),
                    frameId = ++frameCounter,
                    imageB64 = base64Image
                )

                val request = AnalyzeFrameRequest(
                    sessionId = currentSessionId!!,
                    frame = frameData
                )

                val response = apiService.analyzeFitnessFrame(request)
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

    // 3. إنهاء الجلسة
    fun endSession() {
        viewModelScope.launch {
            currentSessionId?.let { sessionId ->
                try {
                    apiService.endFitnessSession(EndSessionRequest(sessionId))
                    // ممكن هنا تنادي على getFitnessSessionSummary لو حابب تعرض تقرير نهائي
                    currentSessionId = null
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}