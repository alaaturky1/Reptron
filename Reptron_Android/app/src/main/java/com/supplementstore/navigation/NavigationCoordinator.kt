package com.supplementstore.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class NavigationCoordinator {
    var currentRoute: AppRoute? by mutableStateOf(null)
        private set
    
    fun navigate(to: AppRoute) {
        currentRoute = to
    }
    
    fun navigateBack() {
        // Navigation back handled by NavController
        // This is kept for API compatibility
    }
    
    fun navigateToRoot() {
        currentRoute = AppRoute.Home
    }
    
    fun navigateToLogin() {
        navigateToRoot()
        navigate(AppRoute.Login)
    }
}
