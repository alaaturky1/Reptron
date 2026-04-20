package com.supplementstore.viewmodels

import androidx.lifecycle.ViewModel
import com.supplementstore.models.Coach
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CoachesViewModel : ViewModel() {
    private val _coaches = MutableStateFlow<List<Coach>>(emptyList())
    val coaches: StateFlow<List<Coach>> = _coaches.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    init {
        loadCoaches()
    }
    
    private fun loadCoaches() {
        // Load coaches from API or local data
        _coaches.value = Coach.sampleCoaches
    }
}
