package com.supplementstore.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.supplementstore.datastore.TokenManager
import com.supplementstore.models.AuthResponse
import com.supplementstore.models.LoginRequest
import com.supplementstore.models.RegisterRequest
import com.supplementstore.services.auth.AuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: AuthResponse) : AuthState()
    data class Error(val message: String) : AuthState()
}

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val authService = AuthService()
    private val tokenManager = TokenManager(application)

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    val userToken = tokenManager.tokenFlow
    val userName = tokenManager.userNameFlow

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            val request = LoginRequest(email, password)
            val result = authService.login(request)

            result.onSuccess { authResponse ->
                // التعديل هنا: بنستنى الحفظ يخلص الأول
                viewModelScope.launch {
                    tokenManager.saveSession(authResponse)
                }.join()

                // بعد ما الحفظ يخلص، نحدث الـ State عشان الشاشة تنقلنا
                _authState.value = AuthState.Success(authResponse)

            }.onFailure { error ->
                _authState.value = AuthState.Error(error.message ?: "حدث خطأ غير معروف")
            }
        }
    }

    fun register(name: String, email: String, password: String, phone: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            val nameParts = name.trim().split(" ")
            val firstName = nameParts.firstOrNull() ?: ""
            val lastName = if (nameParts.size > 1) nameParts.drop(1).joinToString(" ") else firstName
            val userName = email.substringBefore("@")

            val request = RegisterRequest(
                userName = userName,
                email = email,
                password = password,
                firstName = firstName,
                lastName = lastName
            )

            val result = authService.register(request)

            result.onSuccess { authResponse ->
                // التعديل هنا برضو عشان الـ Register
                viewModelScope.launch {
                    tokenManager.saveSession(authResponse)
                }.join()

                _authState.value = AuthState.Success(authResponse)

            }.onFailure { error ->
                _authState.value = AuthState.Error(error.message ?: "حدث خطأ غير معروف")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            // التعديل هنا عشان نضمن إن التوكن القديم اتمسح بالكامل قبل ما نحدث الـ State
            viewModelScope.launch {
                tokenManager.clearSession()
            }.join()

            _authState.value = AuthState.Idle
        }
    }

    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }
}
