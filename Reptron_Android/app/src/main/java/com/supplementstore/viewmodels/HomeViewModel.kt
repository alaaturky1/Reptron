package com.supplementstore.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supplementstore.models.Product
import com.supplementstore.services.api.APIClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    // 1. حالة المنتجات (الأكثر مبيعاً)
    private val _bestSellers = MutableStateFlow<List<Product>>(emptyList())
    val bestSellers: StateFlow<List<Product>> = _bestSellers.asStateFlow()

    // 2. حالة التحميل (Loading)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // 3. حالة الخطأ (Error)
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        refreshHomeData()
    }

    fun refreshHomeData() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response = APIClient.api.getBestSellers(count = 6)
                if (response.isSuccessful) {
                    _bestSellers.value = response.body() ?: emptyList()
                } else {
                    _errorMessage.value = "Failed to load products: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network Error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}