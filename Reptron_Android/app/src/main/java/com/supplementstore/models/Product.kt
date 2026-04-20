package com.supplementstore.models

data class Review(
    val name: String,
    val rating: Int, // 1-5 stars
    val comment: String
)

// Store Product (Simple structure)
data class StoreProduct(
    val id: Int,
    val img: String,
    val name: String,
    val price: Double,
    val oldPrice: Double?
) {
    val onSale: Boolean
        get() = oldPrice != null
    
    companion object {
        val allProducts = listOf(
            StoreProduct(id = 1, img = "product_16", name = "Whey Sport", price = 49.99, oldPrice = 69.99),
            StoreProduct(id = 2, img = "product_17", name = "Whey Protein", price = 39.99, oldPrice = 59.99),
            StoreProduct(id = 3, img = "product_19", name = "Protein Bar", price = 19.99, oldPrice = 29.99),
            StoreProduct(id = 4, img = "product_20", name = "Creatine", price = 29.99, oldPrice = 39.99),
            StoreProduct(id = 5, img = "BCAA Powder", name = "BCAA Powder", price = 34.99, oldPrice = 49.99),
            StoreProduct(id = 6, img = "Pre-Workout", name = "Pre-Workout", price = 44.99, oldPrice = 59.99),
            StoreProduct(id = 7, img = "Glutamine", name = "Glutamine", price = 24.99, oldPrice = 34.99),
            StoreProduct(id = 8, img = "Omega 3 Capsules", name = "Omega 3 Capsules", price = 29.99, oldPrice = 39.99),
            StoreProduct(id = 9, img = "Vitamin D3", name = "Vitamin D3", price = 19.99, oldPrice = 29.99),
            StoreProduct(id = 10, img = "Multivitamins", name = "Multivitamins", price = 27.99, oldPrice = 37.99),
            StoreProduct(id = 11, img = "Weight Gainer", name = "Weight Gainer", price = 54.99, oldPrice = 69.99),
            StoreProduct(id = 12, img = "Electrolyte Drink", name = "Electrolyte Drink", price = 14.99, oldPrice = 22.99)
        )
    }
}

// Full Product (Detailed structure)
data class Product(
    val id: Int,
    val img: String,
    val name: String,
    val price: Double,
    val oldPrice: Double?,
    val description: String,
    val additionalInfo: String? = null,
    val reviews: List<Review>? = null,
    val rating: Double? = null
) {
    val onSale: Boolean
        get() = oldPrice != null
    
    val image: String
        get() = img
    
    val category: String
        get() = "supplements"
    
    companion object {
        val allProducts = listOf(
            Product(
                id = 1, img = "product_16", name = "Whey Sport", price = 49.99, oldPrice = 69.99,
                description = "High-quality whey protein for muscle recovery and growth.",
                additionalInfo = "Net weight: 2 lb (908 g). Serving size: 1 scoop (30 g).",
                reviews = listOf(
                    Review("Jane Doe", 5, "Excellent taste and mixes perfectly!"),
                    Review("John Smith", 4, "Good protein, helps with recovery.")
                )
            ),
            Product(
                id = 2, img = "product_17", name = "Whey Protein", price = 39.99, oldPrice = 59.99,
                description = "Pure whey protein to support daily nutrition and fitness goals.",
                additionalInfo = "Net weight: 1.5 lb (680 g). Serving size: 1 scoop (28 g).",
                reviews = listOf(Review("Ali Hassan", 5, "Very effective protein powder. Great results!"))
            ),
            Product(
                id = 3, img = "product_19", name = "Protein Bar", price = 19.99, oldPrice = 29.99,
                description = "Delicious protein bars packed with essential nutrients.",
                additionalInfo = "Each bar contains 20g protein and 5g fiber.",
                reviews = listOf(Review("Sara Ahmed", 4, "Tasty and convenient snack for busy days."))
            ),
            Product(
                id = 4, img = "product_20", name = "Creatine", price = 29.99, oldPrice = 39.99,
                description = "Enhances strength and power for high-intensity training.",
                additionalInfo = "Net weight: 300g. Take 5g per day mixed with water or juice.",
                reviews = listOf(Review("Mohamed Ali", 5, "Gives great strength boost! Highly recommended."))
            ),
            Product(
                id = 5, img = "BCAA Powder", name = "BCAA Powder", price = 34.99, oldPrice = 49.99,
                description = "Supports muscle recovery and reduces fatigue during workouts.",
                additionalInfo = "Net weight: 400g. Mix 1 scoop with water before or after exercise.",
                reviews = listOf(
                    Review("Ahmed Fathy", 5, "Excellent for recovery, I feel less sore after training."),
                    Review("Mostafa Ali", 4, "Good flavor and helps during long workouts.")
                )
            ),
            Product(
                id = 6, img = "Pre-Workout", name = "Pre-Workout", price = 44.99, oldPrice = 59.99,
                description = "Boosts energy, focus, and endurance during workouts.",
                additionalInfo = "Net weight: 350g. Mix 1 scoop with water 20-30 minutes before training.",
                reviews = listOf(
                    Review("Karim Hassan", 5, "Amazing energy and focus! Best pre-workout I've tried."),
                    Review("Yousef Nabil", 4, "Strong pump and good endurance, but flavor is average.")
                )
            ),
            Product(
                id = 7, img = "Glutamine", name = "Glutamine", price = 24.99, oldPrice = 34.99,
                description = "Supports muscle recovery, immune system, and gut health.",
                additionalInfo = "Net weight: 500g. Take 5g daily after workouts.",
                reviews = listOf(
                    Review("Mahmoud Adel", 5, "Very good for muscle recovery and digestion."),
                    Review("Omar Tarek", 4, "Helped reduce muscle fatigue after heavy workouts.")
                )
            ),
            Product(
                id = 8, img = "Omega 3 Capsules", name = "Omega 3 Capsules", price = 29.99, oldPrice = 39.99,
                description = "Supports heart health, brain function, and joint health.",
                additionalInfo = "Contains 1000mg fish oil per capsule. Take 2 capsules daily with meals.",
                reviews = listOf(
                    Review("Salma Ahmed", 5, "Great for joints and overall health. Highly recommended!"),
                    Review("Hassan Ibrahim", 4, "Good quality and no bad aftertaste.")
                )
            ),
            Product(
                id = 9, img = "Vitamin D3", name = "Vitamin D3", price = 19.99, oldPrice = 29.99,
                description = "Supports bone health, immunity, and muscle function.",
                additionalInfo = "High potency Vitamin D3. Take 1 capsule daily.",
                reviews = listOf(
                    Review("Omar Khaled", 5, "Very good for immunity, I feel more active."),
                    Review("Nour Ahmed", 4, "Great quality and easy to swallow.")
                )
            ),
            Product(
                id = 10, img = "Multivitamins", name = "Multivitamins", price = 27.99, oldPrice = 37.99,
                description = "Complete daily vitamin formula to support overall health.",
                additionalInfo = "One tablet daily after meals.",
                reviews = listOf(
                    Review("Mariam Adel", 5, "Perfect daily supplement, highly recommended!"),
                    Review("Hassan Mostafa", 4, "Good energy boost throughout the day.")
                )
            ),
            Product(
                id = 11, img = "Weight Gainer", name = "Weight Gainer", price = 54.99, oldPrice = 69.99,
                description = "High-calorie mass gainer to support muscle and weight gain.",
                additionalInfo = "Take 2 scoops daily with milk or water.",
                reviews = listOf(
                    Review("Ahmed Samir", 5, "Gained 4kg in one month, amazing results!"),
                    Review("Youssef Tarek", 4, "Good taste and effective for bulking.")
                )
            ),
            Product(
                id = 12, img = "Electrolyte Drink", name = "Electrolyte Drink", price = 14.99, oldPrice = 22.99,
                description = "Rehydrates your body and replaces lost minerals during training.",
                additionalInfo = "Mix 1 scoop with 500ml water during workouts.",
                reviews = listOf(
                    Review("Salma Hassan", 5, "Perfect hydration during intense workouts!"),
                    Review("Karim Nabil", 4, "Refreshing taste and very effective.")
                )
            )
        )
        
        val sample = allProducts[0]
        
        fun findById(id: Int): Product? = allProducts.firstOrNull { it.id == id }
    }
}

data class HomeProduct(
    val id: Int,
    val name: String,
    val category: String,
    val price: Double,
    val rating: Double,
    val image: String,
    val description: String,
    val onSale: Boolean = false,
    val originalPrice: Double? = null
)

data class CartItem(
    val id: Int,
    // السيرفر بيبعت بيانات المنتج جوه الـ Objects دي
    val product: com.supplementstore.models.Product? = null,
    val equipment: com.supplementstore.models.Equipment? = null,

    // هنسيب دول عشان لو السيرفر بعتهم من بره
    val name: String? = null,
    val price: Double? = null,
    var quantity: Int? = null,
    val img: String? = null,
    val image: String? = null,
    val category: String? = null,
    val description: String? = null,
    val oldPrice: Double? = null,
    val onSale: Boolean? = null
) {
    // 🛡️ متغيرات آمنة عشان نحمي التطبيق من أي كراش
    val safeName: String
        get() = product?.name ?: equipment?.name ?: name ?: "Unknown Product"

    val safePrice: Double
        get() = product?.price ?: equipment?.price ?: price ?: 0.0

    val safeQuantity: Int
        get() = quantity ?: 1

    val safeImg: String
        get() = product?.image ?: product?.img ?: equipment?.image ?: img ?: image ?: ""
}
// الكلاس ده هيستقبل الـ Object اللي راجع من السيرفر
data class CartResponse(
    val id: Int? = null,
    val items: List<CartItem>? = null,       // لو السيرفر مسميها items
    val cartItems: List<CartItem>? = null,   // لو السيرفر مسميها cartItems
    val totalPrice: Double? = null
) {
    // دالة مساعدة عشان تجيب الليستة أياً كان اسمها إيه في السيرفر
    fun getProductsList(): List<CartItem> {
        return items ?: cartItems ?: emptyList()
    }
}