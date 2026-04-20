package com.supplementstore.viewmodels

import android.util.Log // ضفنا سطر الطباعة هنا
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supplementstore.models.AddToCartRequest
import com.supplementstore.models.CartItem
import com.supplementstore.models.Product
import com.supplementstore.services.api.APIClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {
    private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
    val cart: StateFlow<List<CartItem>> = _cart.asStateFlow()

    val grandTotal: Double
        get() = _cart.value.sumOf { it.safePrice * it.safeQuantity } // 👈 استخدمنا safe

    val itemsCount: StateFlow<Int> = _cart.asStateFlow().map { cart ->
        cart.sumOf { it.safeQuantity } // 👈 استخدمنا safe
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = 0
    )

    init {
        fetchCart() // بيجيب السلة أول ما تفتح الأبلكيشن
    }

    // 1. جلب بيانات السلة من الـ API
// 1. جلب بيانات السلة من الـ API
    fun fetchCart() {
        viewModelScope.launch {
            try {
                Log.d("CartDebug", "جاري طلب بيانات السلة من السيرفر...")
                val response = APIClient.api.getCart()
                if (response.isSuccessful) {
                    // التعديل هنا: بناخد الـ Response ونجيب منه الليستة
                    val cartResponse = response.body()
                    _cart.value = cartResponse?.getProductsList() ?: emptyList()
                    Log.d("CartDebug", "تم جلب السلة بنجاح! عدد المنتجات: ${_cart.value.size}")
                } else {
                    Log.e("CartDebug", "فشل جلب السلة! كود الخطأ: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("CartDebug", "حصل Exception في جلب السلة: ${e.message}")
            }
        }
    }

    // 2. إضافة منتج جديد للسلة
    fun addProductToCart(product: Product, quantity: Int = 1) {
        viewModelScope.launch {
            try {
                Log.d("CartDebug", "جاري محاولة إضافة المنتج رقم ${product.id} للسلة...")
                val request = AddToCartRequest(
                    itemId = product.id,
                    itemType = "Product",
                    quantity = quantity
                )
                val response = APIClient.api.addToCart(request)
                if (response.isSuccessful) {
                    Log.d("CartDebug", "تمت الإضافة للسيرفر بنجاح!")
                    kotlinx.coroutines.delay(500)
                    fetchCart()
                } else {
                    Log.e(
                        "CartDebug",
                        "فشل الإضافة للسلة! كود الخطأ: ${response.code()}، التفاصيل: ${
                            response.errorBody()?.string()
                        }"
                    )
                }
            } catch (e: Exception) {
                Log.e("CartDebug", "حصل Exception في إضافة المنتج: ${e.message}")
            }
        }
    }

    // 3. زيادة الكمية
    fun increaseQuantity(cartItemId: Int, currentQuantity: Int) {
        viewModelScope.launch {
            try {
                val response = APIClient.api.updateCartItemQuantity(cartItemId, currentQuantity + 1)
                if (response.isSuccessful) fetchCart()
            } catch (e: Exception) {
            }
        }
    }

    // 4. تقليل الكمية
    fun decreaseQuantity(cartItemId: Int, currentQuantity: Int) {
        if (currentQuantity <= 1) {
            removeFromCart(cartItemId)
            return
        }
        viewModelScope.launch {
            try {
                val response = APIClient.api.updateCartItemQuantity(cartItemId, currentQuantity - 1)
                if (response.isSuccessful) fetchCart()
            } catch (e: Exception) {
            }
        }
    }

    // 5. حذف منتج من السلة
    fun removeFromCart(cartItemId: Int) {
        viewModelScope.launch {
            try {
                val response = APIClient.api.removeCartItem(cartItemId)
                if (response.isSuccessful) fetchCart()
            } catch (e: Exception) {
            }
        }
    }

    // دالة إضافة المعدات للسلة
    fun addEquipmentToCart(equipment: com.supplementstore.models.Equipment, quantity: Int = 1) {
        viewModelScope.launch {
            try {
                val request = com.supplementstore.models.AddToCartRequest(
                    itemId = equipment.id,
                    itemType = "Equipment",
                    quantity = quantity
                )
                val response = com.supplementstore.services.api.APIClient.api.addToCart(request)
                if (response.isSuccessful) fetchCart()
            } catch (e: Exception) {
            }
        }
    }

    // دالة تفريغ السلة
    fun clearCart() {
        _cart.value = emptyList()
    }
}