//
//  WorkoutFitnessModels.swift
//  SupplementStore
//

import Foundation

// MARK: - API payloads

/// POST `/api/FitnessCoach/analyze-frame`
struct FitnessCoachAnalyzeFrameRequest: Encodable {
    let sessionId: String
    let frameBase64: String
    let mimeType: String
    let timestamp: TimeInterval

    enum CodingKeys: String, CodingKey {
        case sessionId
        case frameBase64
        case mimeType
        case timestamp
    }
}

/// POST `/api/FitnessCoach/start-session` — flexible decode for common backend shapes.
struct FitnessCoachStartSessionResponse: Decodable {
    let sessionId: String?
    let session_id: String?
    let id: String?

    var resolvedSessionId: String? {
        if let sessionId, !sessionId.isEmpty { return sessionId }
        if let session_id, !session_id.isEmpty { return session_id }
        if let id, !id.isEmpty { return id }
        return nil
    }
}

/// POST `/api/FitnessCoach/end-session`
struct FitnessCoachEndSessionRequest: Encodable {
    let sessionId: String
    let reps: Int
    let score: Int
    let mistakes: [String]

    enum CodingKeys: String, CodingKey {
        case sessionId
        case reps
        case score
        case mistakes
    }
}

/// Response body from end-session (optional fields).
struct FitnessCoachEndSessionResponse: Decodable {
    let feedback: String?
    let message: String?
    let text: String?
    let summary: String?

    var resolvedFeedback: String? {
        let t = feedback ?? text ?? message ?? summary
        let trimmed = t?.trimmingCharacters(in: .whitespacesAndNewlines) ?? ""
        return trimmed.isEmpty ? nil : trimmed
    }
}

/// GET `/api/FitnessCoach/session-summary/{sessionId}`
struct FitnessCoachSessionSummaryDTO: Decodable {
    let id: String?
    let sessionId: String?
    let date: String?
    let reps: Int?
    let repCount: Int?
    let score: Int?
    let mistakes: [String]?
    let feedback: String?
    let feedbackText: String?
}

/// Accepts common backend shapes (camelCase or snake_case, alternate keys).
struct WorkoutAnalyzeResponse: Decodable {
    let repCount: Int?
    let rep_count: Int?
    let reps: Int?
    let state: String?
    let movementState: String?
    let movement_state: String?
    let detectedErrors: [String]?
    let detected_errors: [String]?
    let errors: [String]?

    var normalizedReps: Int {
        repCount ?? rep_count ?? reps ?? 0
    }

    var normalizedState: String {
        let raw = state ?? movementState ?? movement_state ?? "unknown"
        return raw.lowercased()
    }

    var normalizedErrors: [String] {
        if let detectedErrors, !detectedErrors.isEmpty { return detectedErrors }
        if let detected_errors, !detected_errors.isEmpty { return detected_errors }
        return errors ?? []
    }
}

// MARK: - App models

struct WorkoutSessionRecord: Codable, Identifiable, Hashable {
    var id: UUID
    /// Server-side FitnessCoach session id from `start-session`, when available.
    var serverSessionId: String? = nil
    var date: Date
    var reps: Int
    var score: Int
    var mistakes: [String]
    var feedback: String
}

struct FinishedWorkoutSummary: Hashable {
    let id: UUID
    let date: Date
    let totalReps: Int
    let score: Int
    let mistakes: [String]
    let feedbackText: String

    init(record: WorkoutSessionRecord) {
        id = record.id
        date = record.date
        totalReps = record.reps
        score = record.score
        mistakes = record.mistakes
        feedbackText = record.feedback
    }
}
