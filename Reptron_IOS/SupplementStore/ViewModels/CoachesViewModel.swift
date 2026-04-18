//
//  CoachesViewModel.swift
//  SupplementStore
//
//  Created on [Date]
//

import Foundation
import SwiftUI

class CoachesViewModel: ObservableObject {
    @Published var coaches: [Coach] = []
    @Published var isLoading: Bool = false
    @Published var errorMessage: String?
    
    init() {
        loadCoaches()
    }
    
    private func loadCoaches() {
        isLoading = true
        errorMessage = nil
        
        Task {
            do {
                let url = APIEndpoints.url(path: APIEndpoints.Coaches.all)!
                let (data, _) = try await URLSession.shared.data(from: url)
                let rawItems = try JSONSerialization.jsonObject(with: data) as? [[String: Any]] ?? []
                let mapped = rawItems.compactMap { Self.mapCoach(from: $0) }
                
                await MainActor.run {
                    self.coaches = mapped
                    self.isLoading = false
                }
            } catch {
                await MainActor.run {
                    self.coaches = []
                    self.errorMessage = error.localizedDescription
                    self.isLoading = false
                }
            }
        }
    }

    private static func mapCoach(from raw: [String: Any]) -> Coach? {
        let id = raw["id"] as? Int ?? 0
        guard id != 0 else { return nil }
        
        let name = raw["name"] as? String ?? (raw["fullName"] as? String ?? "Coach")
        let specialty = raw["specialty"] as? String ?? (raw["specialization"] as? String ?? "")
        let bio = raw["bio"] as? String ?? (raw["description"] as? String ?? "")
        let image = raw["image"] as? String ?? raw["profilePictureUrl"] as? String ?? ""
        
        return Coach(
            id: id,
            name: name,
            specialty: specialty,
            title: raw["title"] as? String ?? specialty,
            bio: bio,
            fullBio: raw["fullBio"] as? String ?? bio,
            experience: raw["experience"] as? String ?? "",
            clients: raw["clients"] as? String ?? "",
            certifications: raw["certifications"] as? String ?? "",
            image: image,
            phone: raw["phone"] as? String ?? "",
            email: raw["email"] as? String ?? "",
            hourlyRate: raw["hourlyRate"] as? String,
            availability: raw["availability"] as? [String]
        )
    }
}

