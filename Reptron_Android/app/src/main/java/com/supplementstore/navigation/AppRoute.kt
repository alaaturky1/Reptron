package com.supplementstore.navigation

sealed class AppRoute(val route: String) {
    // Public routes
    object Home : AppRoute("home")
    object Login : AppRoute("login")
    object Register : AppRoute("register")
    
    // Account
    object Profile : AppRoute("profile")
    
    // Content
    data class WorkoutProgram(val id: Int = -1) : AppRoute("workout_program/{id}") {
        companion object {
            const val ROUTE = "workout_program/{id}"
            fun createRoute(id: Int) = "workout_program/$id"
        }
    }
    
    // Protected routes
    object AboutUs : AppRoute("about_us")
    object Coaches : AppRoute("coaches")
    data class Coach(val id: Int = -1) : AppRoute("coach/{id}") {
        companion object {
            const val ROUTE = "coach/{id}"
            fun createRoute(id: Int) = "coach/$id"
        }
    }
    data class CoachesProfiles(val id: Int = -1) : AppRoute("coaches_profiles/{id}") {
        companion object {
            const val ROUTE = "coaches_profiles/{id}"
            fun createRoute(id: Int) = "coaches_profiles/$id"
        }
    }
    object Equipments : AppRoute("equipments")
    data class EquipmentDetails(val id: Int = -1) : AppRoute("equipment_details/{id}") {
        companion object {
            const val ROUTE = "equipment_details/{id}"
            fun createRoute(id: Int) = "equipment_details/$id"
        }
    }
    object Store : AppRoute("store")
    data class ProductDetails(val id: Int = -1) : AppRoute("product_details/{id}") {
        companion object {
            const val ROUTE = "product_details/{id}"
            fun createRoute(id: Int) = "product_details/$id"
        }
    }
    object AiCoach : AppRoute("ai_coach")
    object Cart : AppRoute("cart")
    object Checkout : AppRoute("checkout")
    object MyPurchases : AppRoute("my_purchases")
    
    // Error route
    object NotFound : AppRoute("not_found")
}
