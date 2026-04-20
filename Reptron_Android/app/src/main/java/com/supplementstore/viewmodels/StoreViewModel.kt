package com.supplementstore.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supplementstore.models.Product
import com.supplementstore.services.api.APIClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ProductState {
    object Loading : ProductState()
    data class Success(val products: List<Product>) : ProductState()
    data class Error(val message: String) : ProductState()
}

class StoreViewModel : ViewModel() {

    private val _productState = MutableStateFlow<ProductState>(ProductState.Loading)
    val productState: StateFlow<ProductState> = _productState.asStateFlow()

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories.asStateFlow()

    init {
        loadProducts()
        loadCategories()
    }

    fun loadProducts(category: String? = null, search: String? = null) {
        viewModelScope.launch {
            _productState.value = ProductState.Loading
            try {
                val response = APIClient.api.getProducts(category, search)
                if (response.isSuccessful && response.body() != null) {
                    _productState.value = ProductState.Success(response.body()!!)
                } else {
                    _productState.value = ProductState.Error("Failed to load products")
                }
            } catch (e: Exception) {
                _productState.value = ProductState.Error("Check your internet connection")
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            try {
                val response = APIClient.api.getProductCategories()
                if (response.isSuccessful && response.body() != null) {
                    _categories.value = listOf("All") + response.body()!!
                }
            } catch (e: Exception) {
                _categories.value = listOf("All")
            }
        }
    }
}