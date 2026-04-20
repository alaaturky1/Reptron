package com.supplementstore

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.supplementstore.navigation.AppRoute
import com.supplementstore.services.api.APIClient
import com.supplementstore.ui.layout.LayoutView
import com.supplementstore.ui.screens.*
import com.supplementstore.ui.theme.SupplementStoreTheme
import com.supplementstore.viewmodels.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        APIClient.initialize(this)
        setContent {
            SupplementStoreTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    MainApp()
                }
            }
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@androidx.compose.runtime.Composable
fun MainApp() {

    val navController = rememberNavController()

    val homeViewModel: HomeViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()
    val cartViewModel: CartViewModel = viewModel()
    val purchaseViewModel: PurchaseViewModel = viewModel()
    val coachesViewModel: CoachesViewModel = viewModel()
    val equipmentsViewModel: EquipmentsViewModel = viewModel()
    val storeViewModel: StoreViewModel = viewModel()

    val aiCoachViewModel: AiCoachViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return AiCoachViewModel(APIClient.api) as T
            }
        }
    )

    fun navigate(route: AppRoute) {
        when (route) {
            is AppRoute.ProductDetails -> navController.navigate(
                AppRoute.ProductDetails.createRoute(route.id)
            )

            is AppRoute.WorkoutProgram -> navController.navigate(
                AppRoute.WorkoutProgram.createRoute(route.id)
            )

            is AppRoute.Coach -> navController.navigate(AppRoute.Coach.createRoute(route.id))
            is AppRoute.CoachesProfiles -> navController.navigate(
                AppRoute.CoachesProfiles.createRoute(route.id)
            )

            is AppRoute.EquipmentDetails -> navController.navigate(
                AppRoute.EquipmentDetails.createRoute(route.id)
            )

            else -> navController.navigate(route.route)
        }
    }

    NavHost(
        navController = navController, startDestination = AppRoute.Home.route
    ) {

        composable(AppRoute.Home.route) {
            LayoutView(
                content = {
                    HomeView(
                        userViewModel = userViewModel,
                        cartViewModel = cartViewModel,
                        homeViewModel = homeViewModel,
                        onNavigate = { navigate(it) })
                },
                userViewModel = userViewModel,
                cartViewModel = cartViewModel,
                onNavigate = { navigate(it) })
        }

        composable(AppRoute.Store.route) {
            LayoutView(
                content = {
                    StoreView(
                        storeViewModel = storeViewModel,
                        equipmentsViewModel = equipmentsViewModel,
                        onNavigate = { navigate(it) }
                    )
                },
                userViewModel = userViewModel,
                cartViewModel = cartViewModel,
                onNavigate = { navigate(it) })
        }
        composable(AppRoute.Login.route) {
            LayoutView(
                content = {
                    LoginView(
                        userViewModel = userViewModel, onNavigate = { navigate(it) })
                },
                userViewModel = userViewModel,
                cartViewModel = cartViewModel,
                onNavigate = { navigate(it) })
        }

        composable(AppRoute.Register.route) {
            LayoutView(
                content = {
                    RegisterView(
                        userViewModel = userViewModel, onNavigate = { navigate(it) })
                },
                userViewModel = userViewModel,
                cartViewModel = cartViewModel,
                onNavigate = { navigate(it) })
        }

        composable(AppRoute.Profile.route) {
            LayoutView(
                content = {
                    ProfileView(
                        userViewModel = userViewModel, onNavigate = { navigate(it) })
                },
                userViewModel = userViewModel,
                cartViewModel = cartViewModel,
                onNavigate = { navigate(it) })
        }

        composable(AppRoute.Cart.route) {
            LayoutView(
                content = {
                    CartView(
                        cartViewModel = cartViewModel, onNavigate = { navigate(it) })
                },
                userViewModel = userViewModel,
                cartViewModel = cartViewModel,
                onNavigate = { navigate(it) })
        }

        composable(AppRoute.Checkout.route) {
            LayoutView(
                content = {
                    CheckoutView(
                        cartViewModel = cartViewModel,
                        purchaseViewModel = purchaseViewModel,
                        onNavigate = { navigate(it) })
                },
                userViewModel = userViewModel,
                cartViewModel = cartViewModel,
                onNavigate = { navigate(it) })
        }

        composable(AppRoute.MyPurchases.route) {
            LayoutView(
                content = {
                    MyPurchasesView(purchaseViewModel = purchaseViewModel)
                },
                userViewModel = userViewModel,
                cartViewModel = cartViewModel,
                onNavigate = { navigate(it) })
        }

        composable(AppRoute.AboutUs.route) {
            LayoutView(
                content = { AboutUsView() },
                userViewModel = userViewModel,
                cartViewModel = cartViewModel,
                onNavigate = { navigate(it) })
        }

        composable(AppRoute.NotFound.route) {
            LayoutView(
                content = { NotFoundView(onNavigate = { navigate(it) }) },
                userViewModel = userViewModel,
                cartViewModel = cartViewModel,
                onNavigate = { navigate(it) })
        }

        composable(AppRoute.Coaches.route) {
            LayoutView(
                content = {
                    CoachesView(
                        coachesViewModel = coachesViewModel, onNavigate = { navigate(it) })
                },
                userViewModel = userViewModel,
                cartViewModel = cartViewModel,
                onNavigate = { navigate(it) })
        }

        composable(AppRoute.Equipments.route) {
            LayoutView(
                content = {
                    EquipmentsView(
                        equipmentsViewModel = equipmentsViewModel, onNavigate = { navigate(it) })
                },
                userViewModel = userViewModel,
                cartViewModel = cartViewModel,
                onNavigate = { navigate(it) })
        }

        composable(AppRoute.AiCoach.route) {
            LayoutView(
                content = {
                    AiCoachCameraScreen(
                        viewModel = aiCoachViewModel,
                        exerciseName = "squat"
                    )
                },
                userViewModel = userViewModel,
                cartViewModel = cartViewModel,
                onNavigate = { navigate(it) })
        }

        composable(AppRoute.ProductDetails.ROUTE) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: -1
            val product = com.supplementstore.models.Product.allProducts.find { it.id == id }
            if (product != null) {
                LayoutView(
                    content = {
                        ProductDetailsView(
                            product = product,
                            cartViewModel = cartViewModel,
                            onNavigate = { navigate(it) })
                    },
                    userViewModel = userViewModel,
                    cartViewModel = cartViewModel,
                    onNavigate = { navigate(it) })
            }
        }

        composable(AppRoute.WorkoutProgram.ROUTE) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: -1
            val program = com.supplementstore.ui.screens.WorkoutProgram(
                id = id,
                title = "Program $id",
                image = "workout_placeholder",
                description = "Workout Program Details"
            )
            LayoutView(
                content = {
                    WorkoutProgramDetailsView(program = program)
                },
                userViewModel = userViewModel,
                cartViewModel = cartViewModel,
                onNavigate = { navigate(it) })
        }

        composable(AppRoute.Coach.ROUTE) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: -1
            val coach = coachesViewModel.coaches.value.find { it.id == id }
            if (coach != null) {
                LayoutView(
                    content = {
                        CoachDetailsView(
                            coach = coach, onNavigate = { navigate(it) })
                    },
                    userViewModel = userViewModel,
                    cartViewModel = cartViewModel,
                    onNavigate = { navigate(it) })
            }
        }

        composable(AppRoute.EquipmentDetails.ROUTE) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: -1
            val equipment = equipmentsViewModel.equipments.value.find { it.id == id }
            if (equipment != null) {
                LayoutView(
                    content = {
                        EquipmentsDetailsView(
                            equipment = equipment,
                            cartViewModel = cartViewModel,
                            onNavigate = { navigate(it) })
                    },
                    userViewModel = userViewModel,
                    cartViewModel = cartViewModel,
                    onNavigate = { navigate(it) })
            }
        }
    }
}