package com.supplementstore.services.auth

import com.supplementstore.models.AuthResponse
import com.supplementstore.models.LoginRequest
import com.supplementstore.models.RegisterRequest
import com.supplementstore.services.api.APIClient
import org.json.JSONObject

class AuthService {

    suspend fun login(request: LoginRequest): Result<AuthResponse> {
        return try {
            val response = APIClient.api.login(request)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorString = response.errorBody()?.string()
                val errorMessage = try {
                    JSONObject(errorString ?: "").getString("message")
                } catch (e: Exception) {
                    "حدث خطأ أثناء تسجيل الدخول: ${response.code()}"
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception("تأكد من اتصالك بالإنترنت"))
        }
    }

    suspend fun register(request: RegisterRequest): Result<AuthResponse> {
        return try {
            val response = APIClient.api.register(request)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorString = response.errorBody()?.string()
                val errorMessage = try {
                    JSONObject(errorString ?: "").getString("message")
                } catch (e: Exception) {
                    "حدث خطأ أثناء إنشاء الحساب: ${response.code()}"
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception("تأكد من اتصالك بالإنترنت"))
        }
    }
}