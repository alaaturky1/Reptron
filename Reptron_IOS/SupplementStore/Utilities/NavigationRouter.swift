//
//  NavigationRouter.swift
//  SupplementStore
//
//  Created on [Date]
//
//  Navigation router matching React Router structure
//

import SwiftUI

struct NavigationRouter: View {
    @EnvironmentObject var userViewModel: UserViewModel
    @StateObject private var navigationCoordinator = NavigationCoordinator()
    @StateObject private var aiBadgeViewModel = AIBadgeViewModel()
    @StateObject private var workoutHistoryStore = WorkoutHistoryStore()

    var body: some View {
        NavigationStack(path: $navigationCoordinator.path) {
            rootView
                .navigationDestination(for: AppRoute.self) { route in
                    routeView(for: route)
                }
        }
        .environmentObject(AuthSessionManager.shared)
        .environmentObject(navigationCoordinator)
        .environmentObject(aiBadgeViewModel)
        .environmentObject(workoutHistoryStore)
    }

    @ViewBuilder
    private var rootView: some View {
        if userViewModel.isLoggedIn {
            // Logged in: Show MainTabView (matches React Router when authenticated)
            MainTabView()
        } else {
            // Not logged in: Show Home with Layout (public route)
            LayoutView {
                HomeView()
            }
        }
    }
    
    @ViewBuilder
    private func routeView(for route: AppRoute) -> some View {
        switch route {
        // Public routes
        case .home:
            if userViewModel.isLoggedIn {
                MainTabView()
            } else {
                LayoutView {
                    HomeView()
                }
            }
        case .login:
            LayoutView {
                LoginView()
            }
        case .register:
            LayoutView {
                RegisterView()
            }

        case .profile:
            LayoutView {
                ProfileView()
            }

        case .workoutProgram(let id):
            LayoutView {
                if let program = WorkoutProgram.find(by: id) {
                    WorkoutProgramDetailsView(program: program)
                } else {
                    NotFoundView()
                }
            }
            
        // Protected routes - all wrapped in LayoutView to match React structure
        case .aboutUs:
            LayoutView {
                ProtectedRoute {
                    AboutUsView()
                }
            }
        case .coaches:
            LayoutView {
                ProtectedRoute {
                    CoachesView()
                }
            }
        case .coach(_):
            LayoutView {
                ProtectedRoute {
                    CoachDetailsView(coach: Coach.sample) // TODO: Load actual coach by id
                }
            }
        case .coachesProfiles(_):
            LayoutView {
                ProtectedRoute {
                    CoachesProfilesView(coach: Coach.sample) // TODO: Load actual coach by id
                }
            }
        case .equipments:
            LayoutView {
                ProtectedRoute {
                    StoreView(initialSection: .equipment)
                }
            }
        case .equipmentDetails(_):
            LayoutView {
                ProtectedRoute {
                    EquipmentsDetailsView(equipment: Equipment.sample) // TODO: Load actual equipment by id
                }
            }
        case .store:
            LayoutView {
                ProtectedRoute {
                    StoreView()
                }
            }
        case .productDetails(_):
            LayoutView {
                ProtectedRoute {
                    ProductDetailsView(product: Product.sample) // TODO: Load actual product by id
                }
            }
        case .cart:
            LayoutView {
                ProtectedRoute {
                    CartView()
                }
            }
        case .ai:
            LayoutView {
                AIFitnessCoachRootView()
            }
            .toolbar(.hidden, for: .navigationBar)
            .toolbarBackground(.hidden, for: .navigationBar)
        case .checkout:
            LayoutView {
                ProtectedRoute {
                    CheckoutView()
                }
            }
        case .myPurchases:
            LayoutView {
                ProtectedRoute {
                    MyPurchasesView()
                }
            }
            
        // Error route
        case .notFound:
            LayoutView {
                NotFoundView()
            }
        }
    }
}

#Preview {
    NavigationRouter()
        .environmentObject(AuthSessionManager.shared)
        .environmentObject(UserViewModel())
        .environmentObject(CartViewModel())
        .environmentObject(PurchaseViewModel())
        .environmentObject(AIBadgeViewModel())
}

