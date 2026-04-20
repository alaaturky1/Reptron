package com.supplementstore.models

// ريكويست إضافة منتج أو معدات للسلة
data class AddToCartRequest(
    val itemType: String, // غالبا بتكون "Product" أو "Equipment"
    val itemId: Int,
    val quantity: Int
)

// ريكويست عمل أوردر جديد
data class CreateOrderRequest(
    val shippingAddress: String
)

// ريكويست حجز مدرب
data class CreateBookingRequest(
    val coachId: Int,
    val bookingDate: String, // بصيغة "YYYY-MM-DD" زي ما السيرفر طالب
    val bookingTime: String
)

// ريكويست إضافة تقييم
data class CreateReviewRequest(
    val rating: Int,
    val comment: String
)