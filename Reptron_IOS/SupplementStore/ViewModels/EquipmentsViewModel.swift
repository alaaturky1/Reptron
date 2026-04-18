//
//  EquipmentsViewModel.swift
//  SupplementStore
//
//  Created on [Date]
//

import Foundation
import SwiftUI

class EquipmentsViewModel: ObservableObject {
    @Published var equipments: [Equipment] = []
    @Published var isLoading: Bool = false
    @Published var errorMessage: String?
    
    init() {
        loadEquipments()
    }
    
    private func loadEquipments() {
        isLoading = true
        errorMessage = nil
        
        Task {
            do {
                let url = APIEndpoints.url(path: APIEndpoints.Equipment.all)!
                let (data, _) = try await URLSession.shared.data(from: url)
                let rawItems = try JSONSerialization.jsonObject(with: data) as? [[String: Any]] ?? []
                let mapped = rawItems.compactMap { Self.mapEquipment(from: $0) }
                
                await MainActor.run {
                    self.equipments = mapped
                    self.isLoading = false
                }
            } catch {
                await MainActor.run {
                    self.equipments = []
                    self.errorMessage = error.localizedDescription
                    self.isLoading = false
                }
            }
        }
    }

    private static func mapEquipment(from raw: [String: Any]) -> Equipment? {
        let id = raw["id"] as? Int ?? 0
        guard id != 0 else { return nil }
        
        let name = raw["name"] as? String ?? "Equipment"
        let description = raw["description"] as? String ?? (raw["shortDescription"] as? String ?? "")
        let specialty = raw["specialty"] as? String ?? (raw["category"] as? String ?? "")
        
        return Equipment(
            id: id,
            name: name,
            specialty: specialty,
            price: raw["price"] as? Double ?? 0,
            salePrice: raw["salePrice"] as? Double ?? raw["originalPrice"] as? Double,
            image: raw["image"] as? String ?? raw["imageUrl"] as? String ?? "",
            description: description,
            additionalInfo: raw["additionalInfo"] as? String,
            reviews: nil,
            bio: raw["bio"] as? String ?? ""
        )
    }
}

