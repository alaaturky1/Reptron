//
//  CartViewModel.swift
//  SupplementStore
//
//  Created on [Date]
//
//  Shopping cart management matching React CartContext
//  React: { cart, addToCart, removeFromCart, decreaseQuantity, clearCart }
//  Cart items have product properties directly (not nested)
//

import Combine
import Foundation
import SwiftUI

// Cart item structure matching React cart items
// In React, cart items have: { id, name, price, quantity, img, ...productProperties }
struct CartItemModel: Identifiable, Codable, Equatable {
    let id: Int
    let name: String
    let price: Double
    var quantity: Int // Mutable to allow quantity updates
    let img: String
    let category: String?
    let description: String?
    let oldPrice: Double?
    let onSale: Bool?

    // Initialize from Product
    init(from product: Product, quantity: Int = 1) {
        self.id = product.id
        self.name = product.name
        self.price = product.price
        self.quantity = quantity
        self.img = product.image
        self.category = product.category
        self.description = product.description
        self.oldPrice = product.oldPrice
        self.onSale = product.onSale
    }

    // Initialize from Equipment
    init(from equipment: Equipment, quantity: Int = 1) {
        self.id = equipment.id
        self.name = equipment.name
        self.price = equipment.price
        self.quantity = quantity
        self.img = equipment.image
        self.category = equipment.specialty
        self.description = equipment.description
        self.oldPrice = equipment.salePrice
        self.onSale = equipment.salePrice != nil
    }

    // Direct initializer
    init(id: Int, name: String, price: Double, quantity: Int, img: String, category: String? = nil, description: String? = nil, oldPrice: Double? = nil, onSale: Bool? = nil) {
        self.id = id
        self.name = name
        self.price = price
        self.quantity = quantity
        self.img = img
        self.category = category
        self.description = description
        self.oldPrice = oldPrice
        self.onSale = onSale
    }
}

class CartViewModel: ObservableObject {
    @Published var cart: [CartItemModel] = []
    private let apiService = APIService.shared
    private var cancellables = Set<AnyCancellable>()
    private var lastCartUserId: String?

    var grandTotal: Double {
        cart.reduce(0) { $0 + ($1.price * Double($1.quantity)) }
    }

    var itemsCount: Int {
        cart.reduce(0) { $0 + $1.quantity }
    }

    init() {
        lastCartUserId = AuthSessionStorage.bridgedActiveUserId
        NotificationCenter.default.publisher(for: .authSessionDidSignOut)
            .receive(on: DispatchQueue.main)
            .sink { [weak self] _ in
                self?.lastCartUserId = nil
                self?.clearCart()
            }
            .store(in: &cancellables)
        NotificationCenter.default.publisher(for: .authSessionDidChange)
            .receive(on: DispatchQueue.main)
            .sink { [weak self] _ in
                guard let self else { return }
                let uid = AuthSessionStorage.bridgedActiveUserId
                if uid != self.lastCartUserId {
                    self.lastCartUserId = uid
                    self.clearCart()
                }
            }
            .store(in: &cancellables)
    }

    func addToCart(_ product: CartItemModel) {
        if let index = cart.firstIndex(where: { $0.id == product.id }) {
            var updatedItem = cart[index]
            updatedItem.quantity += product.quantity
            cart[index] = updatedItem
        } else {
            cart.append(product)
        }
        Task {
            _ = try? await addCartItemOnBackend(productId: product.id, quantity: product.quantity)
        }
    }

    func addProductToCart(_ product: Product, quantity: Int = 1) {
        let cartItem = CartItemModel(from: product, quantity: quantity)
        addToCart(cartItem)
    }

    func addEquipmentToCart(_ equipment: Equipment, quantity: Int = 1) {
        let cartItem = CartItemModel(from: equipment, quantity: quantity)
        addToCart(cartItem)
    }

    func removeFromCart(_ id: Int) {
        cart.removeAll { $0.id == id }
        Task {
            _ = try? await deleteCartItemOnBackend(itemId: id)
        }
    }

    func decreaseQuantity(_ id: Int) {
        cart = cart.compactMap { item -> CartItemModel? in
            if item.id == id {
                var updated = item
                updated.quantity = item.quantity - 1
                return updated.quantity > 0 ? updated : nil
            }
            return item
        }
        if let updated = cart.first(where: { $0.id == id }) {
            Task {
                _ = try? await updateCartItemOnBackend(itemId: id, quantity: updated.quantity)
            }
        }
    }

    func clearCart() {
        cart = []
    }

    private func addCartItemOnBackend(productId: Int, quantity: Int) async throws -> EmptyCartAPIResponse {
        let body: [String: Any] = [
            "productId": productId,
            "quantity": quantity
        ]
        return try await apiService.post(endpoint: "/api/Cart/items", body: body, requiresAuth: true)
    }

    private func updateCartItemOnBackend(itemId: Int, quantity: Int) async throws -> EmptyCartAPIResponse {
        let body: [String: Any] = [
            "quantity": quantity
        ]
        return try await apiService.put(endpoint: "/api/Cart/items/\(itemId)", body: body, requiresAuth: true)
    }

    private func deleteCartItemOnBackend(itemId: Int) async throws -> EmptyCartAPIResponse {
        return try await apiService.delete(endpoint: "/api/Cart/items/\(itemId)", requiresAuth: true)
    }
}

private struct EmptyCartAPIResponse: Decodable {}
