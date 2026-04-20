package com.supplementstore.models

data class BillingInfo(
    var name: String = "",
    var email: String = "",
    var address: String = "",
    var city: String = "",
    var postalCode: String = "",
    var country: String = ""
)

data class PaymentInfo(
    var cardNumber: String = "",
    var cardName: String = "",
    var expiry: String = "",
    var cvv: String = ""
)

data class ShippingAddress(
    val name: String,
    val address: String,
    val city: String,
    val postalCode: String,
    val country: String
)
