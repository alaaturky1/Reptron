//
//  StoreViewModel.swift
//  SupplementStore
//
//  Created on [Date]
//

import Foundation
import SwiftUI

class StoreViewModel: ObservableObject {
    @Published var products: [Product] = []
    @Published var isLoading: Bool = false
    @Published var errorMessage: String?
    
    init() {
        loadProducts()
    }
    
    private func loadProducts() {
        isLoading = true
        errorMessage = nil
        
        Task {
            do {
                let url = APIEndpoints.url(path: APIEndpoints.Products.all)!
                let (data, _) = try await URLSession.shared.data(from: url)
                let rawItems = try JSONSerialization.jsonObject(with: data) as? [[String: Any]] ?? []
                let mapped = rawItems.compactMap { Self.mapProduct(from: $0) }
                
                await MainActor.run {
                    self.products = mapped
                    self.isLoading = false
                }
            } catch {
                await MainActor.run {
                    self.products = []
                    self.errorMessage = error.localizedDescription
                    self.isLoading = false
                }
            }
        }
    }

    private static func mapProduct(from raw: [String: Any]) -> Product? {
        let id = raw["id"] as? Int ?? raw["productId"] as? Int ?? 0
        guard id != 0 else { return nil }
        
        let name = raw["name"] as? String ?? raw["title"] as? String ?? "Product"
        let description = raw["description"] as? String ?? (raw["shortDescription"] as? String ?? "")
        let price = raw["price"] as? Double ?? raw["unitPrice"] as? Double ?? 0
        let oldPrice = raw["oldPrice"] as? Double ?? raw["originalPrice"] as? Double
        let image = raw["img"] as? String ?? raw["image"] as? String ?? raw["imageUrl"] as? String
        
        return Product(
            id: id,
            img: image,
            name: name,
            price: price,
            oldPrice: oldPrice,
            description: description,
            additionalInfo: raw["additionalInfo"] as? String,
            reviews: nil
        )
    }
}

