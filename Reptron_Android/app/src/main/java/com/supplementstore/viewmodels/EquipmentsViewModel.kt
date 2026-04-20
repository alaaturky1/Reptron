package com.supplementstore.viewmodels

import androidx.lifecycle.ViewModel
import com.supplementstore.models.Equipment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EquipmentsViewModel : ViewModel() {
    private val _equipments = MutableStateFlow<List<Equipment>>(emptyList())
    val equipments: StateFlow<List<Equipment>> = _equipments.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadEquipments()
    }

    private fun loadEquipments() {
        // Load equipments from API or local data
        _equipments.value = Equipment.sampleEquipments
    }
}
