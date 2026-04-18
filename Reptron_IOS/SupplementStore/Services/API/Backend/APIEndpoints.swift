import Foundation

/// API base matches Swagger: `http://gym-management-0.runasp.net/swagger`
enum APIEndpoints {
    static let baseURL = "http://gym-management-0.runasp.net"

    /// Builds an absolute URL from a path like `/api/Products` (single source of truth for the host).
    static func url(path: String) -> URL? {
        let trimmedBase = baseURL.trimmingCharacters(in: CharacterSet(charactersIn: "/"))
        let p = path.hasPrefix("/") ? path : "/" + path
        return URL(string: "\(trimmedBase)\(p)")
    }

    /// Fitness coach (same host as the rest of the app). See `/api/FitnessCoach/*` in Swagger.
    enum AI {
        /// JSON with `count`, `unreadCount`, `unread`, `badge`, or `total` — adjust if Swagger differs.
        static let badgeCountPath = "/api/badge-count"

        static let startSession = "/api/FitnessCoach/start-session"
        static let analyzeFrame = "/api/FitnessCoach/analyze-frame"
        static let endSession = "/api/FitnessCoach/end-session"
        static func sessionSummary(_ sessionId: String) -> String {
            "/api/FitnessCoach/session-summary/\(sessionId)"
        }

        static var badgeCountURL: URL? {
            APIEndpoints.url(path: badgeCountPath)
        }
    }

    enum Auth {
        static let login = "/api/Auth/login"
        static let register = "/api/Auth/register"
        /// Not in published Swagger yet; server should expose `POST` with `currentPassword` + `newPassword` (JWT).
        static let changePassword = "/api/Auth/change-password"
    }

    enum Products {
        static let all = "/api/Products"
        static func byId(_ id: Int) -> String { "/api/Products/\(id)" }
        static let bestSellers = "/api/Products/best-sellers"
    }

    enum Cart {
        static let current = "/api/Cart"
        static let items = "/api/Cart/items"
        static func item(_ cartItemId: Int) -> String { "/api/Cart/items/\(cartItemId)" }
    }

    enum Orders {
        static let create = "/api/Orders"
        static let all = "/api/Orders"
        static func byId(_ orderId: Int) -> String { "/api/Orders/\(orderId)" }
    }

    enum Reviews {
        static func product(_ productId: Int) -> String { "/api/Reviews/products/\(productId)" }
        static func equipment(_ equipmentId: Int) -> String { "/api/Reviews/equipment/\(equipmentId)" }
    }

    enum Coaches {
        static let all = "/api/Coaches"
        static func byId(_ id: Int) -> String { "/api/Coaches/\(id)" }
        static func availability(_ id: Int) -> String { "/api/Coaches/\(id)/availability" }
        static func bookings(_ id: Int) -> String { "/api/Coaches/\(id)/bookings" }
    }

    enum Equipment {
        static let all = "/api/Equipments"
        static func byId(_ id: Int) -> String { "/api/Equipments/\(id)" }
    }

    enum Categories {
        static let products = "/api/Categories/products"
        static let equipment = "/api/Categories/equipment"
    }
}
