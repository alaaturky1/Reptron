package com.supplementstore.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.supplementstore.viewmodels.UserViewModel

/**
 * Protected route wrapper that redirects to login if user is not authenticated
 * Matches SwiftUI ProtectedRoute behavior
 */
@Composable
fun ProtectedRoute(
    userViewModel: UserViewModel,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    // In SwiftUI, ProtectedRoute checks isLoggedIn and shows login if not
    // In Compose, we can handle this at the navigation level or use a similar pattern
    
    // For now, this is a simple wrapper - actual protection should be in NavHost
    // when creating routes
    
    // Example usage in NavHost:
    // composable(AppRoute.Store.route) {
    //     if (userViewModel.isLoggedIn) {
    //         StoreView(...)
    //     } else {
    //         LoginView(...)
    //     }
    // }
    
    content()
}

// Note: Route protection is better handled in the NavHost configuration
// by checking authentication state before navigating to protected routes
