package com.supplementstore.models

data class Equipment(
    val id: Int,
    val name: String,
    val specialty: String,
    val price: Double,
    val salePrice: Double? = null,
    val image: String,
    val description: String,
    val additionalInfo: String? = null,
    val reviews: List<Review>? = null,
    val bio: String
) {
    companion object {
        val sample = Equipment(
            id = 1,
            name = "Chest Press Machine",
            specialty = "Chest",
            price = 499.99,
            salePrice = 599.99,
            image = "chest_press",
            description = "Premium chest press machine",
            additionalInfo = "Material: Steel",
            reviews = emptyList(),
            bio = "Certified trainer with 8 years of experience"
        )
        
        val sampleEquipments = listOf(sample)
    }
}
