//
//  WorkoutFitnessModels.swift
//  SupplementStore
//

import Foundation

// MARK: - API payloads

/// POST `/api/FitnessCoach/analyze-frame`
struct FitnessCoachAnalyzeFrameRequest: Encodable {
    let session_id: String
    let frame: FitnessCoachFramePayload

    enum CodingKeys: String, CodingKey {
        case session_id
        case frame
    }
}

struct FitnessCoachFramePayload: Encodable {
    let exercise: String
    let timestamp: TimeInterval
    let frame_id: Int?
    let image_b64: String?
    let joints: [FitnessCoachJointPayload]?
    let angles: [String: Double]?

    init(
        exercise: String,
        timestamp: TimeInterval,
        frame_id: Int? = nil,
        image_b64: String? = nil,
        joints: [FitnessCoachJointPayload]? = nil,
        angles: [String: Double]? = nil
    ) {
        self.exercise = exercise
        self.timestamp = timestamp
        self.frame_id = frame_id
        self.image_b64 = image_b64
        self.joints = joints
        self.angles = angles
    }
}

struct FitnessCoachJointPayload: Encodable {
    let name: String
    let x: Double
    let y: Double
    let z: Double?
    let confidence: Double?
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
    let session_id: String

    enum CodingKeys: String, CodingKey {
        case session_id
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
    let session_id: String?
    let exercise: String?
    let reps: Int?
    let avg_rep_score: Double?
    let best_rep_score: Double?
    let worst_rep_score: Double?
    let most_frequent_mistake: String?
    let active_time_s: Double?
    let idle_time_s: Double?
    let rep_summaries: [FitnessCoachRepSummaryDTO]?
    let issues_tally: [String: Int]?
    let feedback: String?

    var resolvedScore: Int? {
        if let avg_rep_score {
            return Int(avg_rep_score.rounded())
        }
        if let best_rep_score {
            return Int(best_rep_score.rounded())
        }
        return nil
    }

    var resolvedDurationSeconds: Double {
        max(0, (active_time_s ?? 0) + (idle_time_s ?? 0))
    }

    var resolvedMistakes: [String] {
        if let tally = issues_tally {
            let top = tally
                .filter { !Self.operationalIssues.contains($0.key) }
                .sorted { $0.value > $1.value }
                .prefix(3)
                .map(\.key)
            if !top.isEmpty { return top }
        }
        if let most_frequent_mistake, !Self.operationalIssues.contains(most_frequent_mistake) {
            return [most_frequent_mistake]
        }
        return []
    }

    func resolvedFeedback(fallback: String) -> String {
        let trimmed = feedback?.trimmingCharacters(in: .whitespacesAndNewlines) ?? ""
        return trimmed.isEmpty ? fallback : trimmed
    }

    private static let operationalIssues: Set<String> = [
        "visibility_low",
        "unknown_exercise",
        "pose_detection_failed",
        "pose_detection_error"
    ]
}

struct FitnessCoachRepSummaryDTO: Decodable {
    let rep_index: Int
    let score: Double
    let issues: [String]
}

/// Accepts common backend shapes (camelCase or snake_case, alternate keys).
struct WorkoutAnalyzeResponse: Decodable {
    let feedback: String?
    let score: Double?
    let repCount: Int?
    let rep_count: Int?
    let reps: Int?
    let exercise: String?
    let state: String?
    let movementState: String?
    let movement_state: String?
    let detectedErrors: [String]?
    let detected_errors: [String]?
    let errors: [String]?
    let issues: [String]?
    let paused: Bool?
    let speak: Bool?
    let priority: String?
    let lang: String?
    let debug: [String: FitnessCoachJSONValue]?

    var normalizedReps: Int {
        repCount ?? rep_count ?? reps ?? 0
    }

    var normalizedScore: Int? {
        guard let score else { return nil }
        return max(0, min(100, Int(score.rounded())))
    }

    var normalizedState: String {
        let raw = state ?? movementState ?? movement_state ?? debugString("phase") ?? (paused == true ? "paused" : "live")
        return raw.lowercased()
    }

    var normalizedErrors: [String] {
        if let issues, !issues.isEmpty { return issues }
        if let detectedErrors, !detectedErrors.isEmpty { return detectedErrors }
        if let detected_errors, !detected_errors.isEmpty { return detected_errors }
        return errors ?? []
    }

    var normalizedFeedback: String? {
        let trimmed = feedback?.trimmingCharacters(in: .whitespacesAndNewlines) ?? ""
        return trimmed.isEmpty ? nil : trimmed
    }

    var normalizedLanguage: String {
        lang ?? "en"
    }

    private func debugString(_ key: String) -> String? {
        guard let value = debug?[key] else { return nil }
        if case .string(let text) = value {
            return text
        }
        return nil
    }
}

enum FitnessCoachJSONValue: Decodable {
    case string(String)
    case int(Int)
    case double(Double)
    case bool(Bool)
    case object([String: FitnessCoachJSONValue])
    case array([FitnessCoachJSONValue])
    case null

    init(from decoder: Decoder) throws {
        let container = try decoder.singleValueContainer()

        if container.decodeNil() {
            self = .null
        } else if let value = try? container.decode(Bool.self) {
            self = .bool(value)
        } else if let value = try? container.decode(Int.self) {
            self = .int(value)
        } else if let value = try? container.decode(Double.self) {
            self = .double(value)
        } else if let value = try? container.decode(String.self) {
            self = .string(value)
        } else if let value = try? container.decode([String: FitnessCoachJSONValue].self) {
            self = .object(value)
        } else if let value = try? container.decode([FitnessCoachJSONValue].self) {
            self = .array(value)
        } else {
            throw DecodingError.typeMismatch(
                FitnessCoachJSONValue.self,
                DecodingError.Context(codingPath: decoder.codingPath, debugDescription: "Unsupported fitness coach debug value")
            )
        }
    }
}

// MARK: - App models

struct WorkoutSessionRecord: Codable, Identifiable, Hashable {
    var id: UUID
    /// Server-side FitnessCoach session id from `start-session`, when available.
    var serverSessionId: String? = nil
    var exercise: String = "squat"
    var date: Date
    var reps: Int
    var score: Int
    var mistakes: [String]
    var feedback: String
    var durationSeconds: Double

    enum CodingKeys: String, CodingKey {
        case id, serverSessionId, exercise, date, reps, score, mistakes, feedback, durationSeconds
    }

    init(
        id: UUID,
        serverSessionId: String? = nil,
        exercise: String = "squat",
        date: Date,
        reps: Int,
        score: Int,
        mistakes: [String],
        feedback: String,
        durationSeconds: Double = 0
    ) {
        self.id = id
        self.serverSessionId = serverSessionId
        self.exercise = exercise
        self.date = date
        self.reps = reps
        self.score = score
        self.mistakes = mistakes
        self.feedback = feedback
        self.durationSeconds = durationSeconds
    }

    init(from decoder: Decoder) throws {
        let c = try decoder.container(keyedBy: CodingKeys.self)
        id = try c.decode(UUID.self, forKey: .id)
        serverSessionId = try c.decodeIfPresent(String.self, forKey: .serverSessionId)
        exercise = try c.decodeIfPresent(String.self, forKey: .exercise) ?? "squat"
        date = try c.decode(Date.self, forKey: .date)
        reps = try c.decode(Int.self, forKey: .reps)
        score = try c.decode(Int.self, forKey: .score)
        mistakes = try c.decode([String].self, forKey: .mistakes)
        feedback = try c.decode(String.self, forKey: .feedback)
        durationSeconds = try c.decodeIfPresent(Double.self, forKey: .durationSeconds) ?? 0
    }
}

struct FinishedWorkoutSummary: Hashable {
    let id: UUID
    let date: Date
    let exercise: String
    let totalReps: Int
    let score: Int
    let mistakes: [String]
    let feedbackText: String
    let durationSeconds: Double

    init(record: WorkoutSessionRecord) {
        id = record.id
        date = record.date
        exercise = record.exercise
        totalReps = record.reps
        score = record.score
        mistakes = record.mistakes
        feedbackText = record.feedback
        durationSeconds = record.durationSeconds
    }
}
