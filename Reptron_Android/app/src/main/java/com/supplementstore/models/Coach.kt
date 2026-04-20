package com.supplementstore.models

data class Coach(
    val id: Int,
    val name: String,
    val specialty: String,
    val title: String,
    val bio: String,
    val fullBio: String,
    val experience: String,
    val clients: String,
    val certifications: String,
    val image: String,
    val phone: String,
    val email: String,
    val hourlyRate: String? = null,
    val availability: List<String>? = null
) {
    companion object {
        val sample = Coach(
            id = 1,
            name = "Ahmed Mohamed",
            specialty = "Bodybuilding",
            title = "Professional Bodybuilding Coach",
            bio = "Certified trainer with 8 years of experience",
            fullBio = "Ahmed is a professional bodybuilding coach",
            experience = "8+ years",
            clients = "150+ clients",
            certifications = "5 championships",
            image = "coach_1",
            phone = "+20 123 456 7890",
            email = "ahmed@example.com",
            hourlyRate = "$50/hour",
            availability = listOf("Monday: 9AM - 5PM")
        )
        
        val sampleCoaches = listOf(sample)
    }
}
