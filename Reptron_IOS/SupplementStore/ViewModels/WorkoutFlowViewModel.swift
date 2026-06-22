//
//  WorkoutFlowViewModel.swift
//  SupplementStore
//

import Foundation
import SwiftUI

@MainActor
final class WorkoutFlowViewModel: ObservableObject {
    @Published var isFinishing = false
    @Published var finishError: String?
    @Published private(set) var sessionStartError: String?

    private let api = AIFitnessAPIService.shared
    private let hub = WorkoutAnalysisHub.shared

    /// Set when `finishWorkout` runs so `onLiveSessionDisappeared` does not call `end-session` again.
    private var liveSessionFinished = false

    func startLiveSession() async {
        liveSessionFinished = false
        sessionStartError = nil
        hub.setActiveCoachSessionId(nil)
        do {
            let sid = try await api.startCoachSession(
                language: hub.selectedLanguage,
                level: normalizedLevel(hub.selectedLevel)
            )
            hub.setActiveCoachSessionId(sid)
        } catch {
            sessionStartError = error.localizedDescription
            hub.setActiveCoachSessionId(nil)
        }
    }

    /// Call when the live camera screen is dismissed without completing `finishWorkout` (e.g. Back).
    func onLiveSessionDisappeared() async {
        guard !liveSessionFinished else { return }
        guard let sid = hub.activeCoachSessionId else { return }
        let reps = hub.repCount
        let mistakes = hub.detectedErrors
        let score = currentScore(mistakes: mistakes)
        try? await api.endCoachSession(sessionId: sid, reps: reps, score: score, mistakes: mistakes)
        hub.setActiveCoachSessionId(nil)
    }

    func finishWorkout(history: WorkoutHistoryStore) async -> FinishedWorkoutSummary? {
        guard !isFinishing else { return nil }
        isFinishing = true
        finishError = nil
        liveSessionFinished = true
        defer { isFinishing = false }

        var reps = hub.repCount
        var mistakes = hub.detectedErrors
        var score = currentScore(mistakes: mistakes)
        var durationSeconds: Double = 0
        let serverSid = hub.activeCoachSessionId
        let exercise = hub.selectedExercise

        let fallbackFeedback = hub.lastFeedback ?? defaultFeedback(exercise: exercise, reps: reps, score: score, durationSeconds: durationSeconds)
        var feedbackText = fallbackFeedback
        if let sid = serverSid {
            do {
                if let summary = try await api.endCoachSession(sessionId: sid, reps: reps, score: score, mistakes: mistakes) {
                    if let serverReps = summary.reps { reps = serverReps }
                    if let serverScore = summary.resolvedScore { score = serverScore }
                    let serverMistakes = summary.resolvedMistakes
                    if !serverMistakes.isEmpty { mistakes = serverMistakes }
                    durationSeconds = summary.resolvedDurationSeconds
                    let fallback = hub.lastFeedback ?? defaultFeedback(
                        exercise: exercise,
                        reps: reps,
                        score: score,
                        durationSeconds: durationSeconds
                    )
                    feedbackText = summary.resolvedFeedback(fallback: fallback)
                }
            } catch {
                feedbackText = fallbackFeedback
            }
        }

        let record = WorkoutSessionRecord(
            id: UUID(),
            serverSessionId: serverSid,
            exercise: exercise,
            date: Date(),
            reps: reps,
            score: score,
            mistakes: mistakes,
            feedback: feedbackText,
            durationSeconds: durationSeconds
        )

        history.add(record)

        let summary = FinishedWorkoutSummary(record: record)
        hub.resetForNewWorkout()
        return summary
    }

    private func currentScore(mistakes: [String]) -> Int {
        if hub.score > 0 { return hub.score }
        return max(0, min(100, 100 - mistakes.count * 10))
    }

    private func normalizedLevel(_ level: String) -> String {
        level == "professional" ? "advanced" : level
    }

    private func defaultFeedback(exercise: String, reps: Int, score: Int, durationSeconds: Double) -> String {
        if exercise == "plank" {
            let seconds = Int(durationSeconds.rounded())
            return "Session complete: \(seconds)s hold, score \(score). Keep your core braced and your body in one line."
        }
        return "Session complete: \(reps) reps, score \(score). Focus on steady tempo and full range of motion."
    }
}
