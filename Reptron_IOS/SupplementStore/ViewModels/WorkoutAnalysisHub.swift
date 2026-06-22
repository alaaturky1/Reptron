//
//  WorkoutAnalysisHub.swift
//  SupplementStore
//

import AVFoundation
import Foundation
import SwiftUI

@MainActor
final class WorkoutAnalysisHub: ObservableObject {
    static let shared = WorkoutAnalysisHub()

    @Published private(set) var repCount: Int = 0
    @Published private(set) var movementState: String = "—"
    @Published private(set) var detectedErrors: [String] = []
    @Published private(set) var lastAnalyzeError: String?
    @Published private(set) var isAnalyzing: Bool = false
    @Published private(set) var score: Int = 0
    @Published private(set) var lastFeedback: String?
    @Published private(set) var isPaused: Bool = false
    @Published private(set) var shouldSpeak: Bool = false
    @Published private(set) var feedbackPriority: String = "low"
    @Published private(set) var responseLanguage: String = "en"
    @Published var selectedExercise: String = "squat"
    @Published var selectedLanguage: String = "en"
    @Published var selectedLevel: String = "beginner"
    /// Set after `POST /api/FitnessCoach/start-session`; required for `analyze-frame`.
    @Published private(set) var activeCoachSessionId: String?
    @Published private(set) var sessionStartedAt: Date?

    private let speaker = WorkoutCoachSpeaker()

    var formLooksGood: Bool {
        detectedErrors.isEmpty && lastAnalyzeError == nil && !isPaused
    }

    private init() {}

    func applyAnalyzeResponse(_ response: WorkoutAnalyzeResponse) {
        lastAnalyzeError = nil
        isAnalyzing = false
        repCount = max(repCount, response.normalizedReps)
        movementState = response.normalizedState
        detectedErrors = response.normalizedErrors
        if let nextScore = response.normalizedScore {
            score = nextScore
        }
        if let feedback = response.normalizedFeedback {
            lastFeedback = feedback
        }
        isPaused = response.paused ?? false
        shouldSpeak = response.speak ?? false
        feedbackPriority = response.priority ?? "low"
        responseLanguage = response.normalizedLanguage

        if shouldSpeak, let feedback = response.normalizedFeedback {
            speaker.speak(feedback, language: response.normalizedLanguage, priority: feedbackPriority)
        }
    }

    func setAnalyzeError(_ message: String) {
        isAnalyzing = false
        lastAnalyzeError = message
    }

    func markAnalyzing() {
        isAnalyzing = true
    }

    /// Clears the analyzing flag when a frame is skipped (e.g. session not ready yet).
    func abortAnalyzing() {
        isAnalyzing = false
    }

    func resetForNewWorkout() {
        repCount = 0
        movementState = "—"
        detectedErrors = []
        lastAnalyzeError = nil
        isAnalyzing = false
        score = 0
        lastFeedback = nil
        isPaused = false
        shouldSpeak = false
        feedbackPriority = "low"
        responseLanguage = selectedLanguage
        activeCoachSessionId = nil
        sessionStartedAt = nil
    }

    func setActiveCoachSessionId(_ id: String?) {
        activeCoachSessionId = id
        sessionStartedAt = id == nil ? nil : Date()
    }
}

private final class WorkoutCoachSpeaker {
    private let synthesizer = AVSpeechSynthesizer()

    func speak(_ text: String, language: String, priority: String) {
        let trimmed = text.trimmingCharacters(in: .whitespacesAndNewlines)
        guard !trimmed.isEmpty else { return }

        if synthesizer.isSpeaking {
            if priority == "high" {
                synthesizer.stopSpeaking(at: .immediate)
            } else {
                return
            }
        }

        let utterance = AVSpeechUtterance(string: trimmed)
        utterance.voice = AVSpeechSynthesisVoice(language: language == "ar" ? "ar-EG" : "en-US")
        utterance.rate = AVSpeechUtteranceDefaultSpeechRate * 0.92
        synthesizer.speak(utterance)
    }
}
