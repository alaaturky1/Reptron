package com.supplementstore.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.supplementstore.models.AuthResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class TokenManager(private val context: Context) {

    companion object {
        val TOKEN_KEY = stringPreferencesKey("jwt_token")
        val USER_ID_KEY = stringPreferencesKey("user_id")
        val USER_NAME_KEY = stringPreferencesKey("user_name")
        val EMAIL_KEY = stringPreferencesKey("email")
        val ROLE_KEY = stringPreferencesKey("role")
    }

    // دالة لحفظ بيانات اليوزر بعد ما يعمل Login أو Register بنجاح
    suspend fun saveSession(user: AuthResponse) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = user.token
            prefs[USER_ID_KEY] = user.userId
            prefs[USER_NAME_KEY] = user.userName
            prefs[EMAIL_KEY] = user.email
            prefs[ROLE_KEY] = user.role
        }
    }

    // دي Flow بتقرأ الـ Token لو موجود (بترجع null لو اليوزر مش عامل تسجيل دخول)
    val tokenFlow: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[TOKEN_KEY]
    }

    // دي Flow بتقرأ اسم اليوزر عشان تعرضه في شاشة الـ Profile أو الـ Home
    val userNameFlow: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[USER_NAME_KEY]
    }

    // دالة لمسح البيانات (لما اليوزر يعمل تسجيل خروج - Logout)
    suspend fun clearSession() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}