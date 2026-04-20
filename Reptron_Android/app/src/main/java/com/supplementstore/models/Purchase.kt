package com.supplementstore.models

data class Purchase(
    val id: Int,
    val date: String, // Stored as string to match iOS format
    val items: List<CartItem>,
    val total: Double,
    val shippingAddress: ShippingAddress
)
