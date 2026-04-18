//
//  EquipmentsDetailsView.swift
//  SupplementStore
//
//  Created on [Date]
//

import SwiftUI
import UIKit

// MARK: - Device Size Helper
// Local implementation of DeviceSize for EquipmentsDetailsView
private enum DeviceSize {
    // Base values are for iPhone 12/13/14 (390x844 points)
    private static let baseScreenWidth: CGFloat = 390
    
    // Calculate a scaled value based on the current device width
    private static func scaleValue(_ value: CGFloat) -> CGFloat {
        let screenWidth = UIScreen.main.bounds.width
        return value * (screenWidth / baseScreenWidth)
    }
    
    // MARK: - Spacing
    static func spacing(base: CGFloat) -> CGFloat { scaleValue(base) }
    
    // MARK: - Padding
    static func padding(base: CGFloat) -> CGFloat { scaleValue(base) }
    
    // MARK: - Font Size
    static func fontSize(base: CGFloat) -> CGFloat { scaleValue(base) }
    
    // MARK: - Corner Radius
    static func cornerRadius(base: CGFloat) -> CGFloat { scaleValue(base) }
}

struct EquipmentsDetailsView: View {
    let equipment: Equipment
    @EnvironmentObject var cartViewModel: CartViewModel
    @EnvironmentObject var navigationCoordinator: NavigationCoordinator
    @State private var quantity: Int = 1
    @State private var selectedTab: EquipmentTab = .description
    
    enum EquipmentTab {
        case description, additional, reviews
    }
    
    var body: some View {
        ScrollView {
            VStack(spacing: 0) {
                // Equipment Image
                APIReadyImageView(
                    imagePath: equipment.image,
                    placeholderSystemName: "dumbbell.fill",
                    height: 300
                )
                .frame(maxWidth: .infinity)
                .background(Color(red: 30/255, green: 41/255, blue: 59/255))
                
                VStack(spacing: DeviceSize.spacing(base: 24)) {
                    // Equipment Name and Price
                    VStack(alignment: .leading, spacing: DeviceSize.spacing(base: 12)) {
                        Text(equipment.name)
                            .font(.system(size: DeviceSize.fontSize(base: 28), weight: .bold))
                            .foregroundColor(.white)
                        
                        HStack(spacing: DeviceSize.spacing(base: 16)) {
                            Text("$\(String(format: "%.2f", equipment.price))")
                                .font(.system(size: DeviceSize.fontSize(base: 32), weight: .heavy))
                                .foregroundStyle(
                                    LinearGradient(
                                        colors: [Color.cyan, Color(red: 0, green: 188/255, blue: 212/255)],
                                        startPoint: .leading,
                                        endPoint: .trailing
                                    )
                                )
                            
                            if let salePrice = equipment.salePrice {
                                Text("$\(String(format: "%.2f", salePrice))")
                                    .font(.system(size: DeviceSize.fontSize(base: 20)))
                                    .foregroundColor(.red)
                                    .strikethrough()
                            }
                        }
                    }
                    .frame(maxWidth: .infinity, alignment: .leading)
                    
                    // Quantity Selector
                    HStack(spacing: DeviceSize.spacing(base: 20)) {
                        Text("Quantity:")
                            .font(.system(size: DeviceSize.fontSize(base: 18), weight: .semibold))
                            .foregroundColor(.white)
                        
                        HStack(spacing: DeviceSize.spacing(base: 16)) {
                            Button(action: {
                                if quantity > 1 {
                                    quantity -= 1
                                }
                            }) {
                                Image(systemName: "minus.circle.fill")
                                    .font(.system(size: DeviceSize.fontSize(base: 24)))
                                    .foregroundColor(Color.cyan)
                            }
                            
                            Text("\(quantity)")
                                .font(.system(size: DeviceSize.fontSize(base: 20), weight: .bold))
                                .foregroundColor(.white)
                                .frame(minWidth: 40)
                            
                            Button(action: {
                                quantity += 1
                            }) {
                                Image(systemName: "plus.circle.fill")
                                    .font(.system(size: DeviceSize.fontSize(base: 24)))
                                    .foregroundColor(Color.cyan)
                            }
                        }
                        
                        Spacer()
                    }
                    
                    // Tabs
                    HStack(spacing: 0) {
                        TabButton(title: "Description", isSelected: selectedTab == .description) {
                            selectedTab = .description
                        }
                        TabButton(title: "Additional", isSelected: selectedTab == .additional) {
                            selectedTab = .additional
                        }
                        TabButton(title: "Reviews", isSelected: selectedTab == .reviews) {
                            selectedTab = .reviews
                        }
                    }
                    .padding(.vertical, DeviceSize.padding(base: 8))
                    
                    // Tab Content
                    Group {
                        switch selectedTab {
                        case .description:
                            Text(equipment.description)
                                .font(.system(size: DeviceSize.fontSize(base: 16)))
                                .foregroundColor(Color(red: 203/255, green: 213/255, blue: 225/255))
                                .frame(maxWidth: .infinity, alignment: .leading)
                            
                        case .additional:
                            if let additionalInfo = equipment.additionalInfo {
                                Text(additionalInfo)
                                    .font(.system(size: DeviceSize.fontSize(base: 16)))
                                    .foregroundColor(Color(red: 203/255, green: 213/255, blue: 225/255))
                                    .frame(maxWidth: .infinity, alignment: .leading)
                            } else {
                                Text("No additional information available.")
                                    .font(.system(size: DeviceSize.fontSize(base: 16)))
                                    .foregroundColor(Color(red: 203/255, green: 213/255, blue: 225/255).opacity(0.7))
                            }
                            
                        case .reviews:
                            if let reviews = equipment.reviews, !reviews.isEmpty {
                                VStack(alignment: .leading, spacing: DeviceSize.spacing(base: 16)) {
                                    ForEach(reviews) { review in
                                        ReviewRow(review: review)
                                    }
                                }
                            } else {
                                Text("No reviews yet.")
                                    .font(.system(size: DeviceSize.fontSize(base: 16)))
                                    .foregroundColor(Color(red: 203/255, green: 213/255, blue: 225/255).opacity(0.7))
                            }
                        }
                    }
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(.top, DeviceSize.padding(base: 16))
                    
                    // Add to Cart Button
                    Button(action: {
                        cartViewModel.addEquipmentToCart(equipment, quantity: quantity)
                        navigationCoordinator.navigate(to: .cart)
                    }) {
                        HStack {
                            Image(systemName: "cart.badge.plus")
                            Text("Add to Cart")
                                .font(.system(size: DeviceSize.fontSize(base: 18), weight: .semibold))
                        }
                        .foregroundColor(Color(red: 15/255, green: 23/255, blue: 42/255))
                        .frame(maxWidth: .infinity)
                        .padding(.vertical, DeviceSize.padding(base: 16))
                        .background(
                            LinearGradient(
                                colors: [Color.cyan, Color(red: 0, green: 188/255, blue: 212/255)],
                                startPoint: .leading,
                                endPoint: .trailing
                            )
                        )
                        .cornerRadius(12)
                    }
                    .padding(.top, DeviceSize.padding(base: 24))

                    PageFooterView()
                }
                .padding(DeviceSize.padding(base: 24))
            }
        }
        .background(
            LinearGradient(
                colors: [
                    Color(red: 15/255, green: 23/255, blue: 42/255),
                    Color(red: 30/255, green: 41/255, blue: 59/255)
                ],
                startPoint: .top,
                endPoint: .bottom
            )
        )
        .ignoresSafeArea(edges: .top)
    }
}

#Preview {
    EquipmentsDetailsView(equipment: Equipment.sample)
        .environmentObject(CartViewModel())
        .environmentObject(NavigationCoordinator())
}
