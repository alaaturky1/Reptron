package com.supplementstore.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supplementstore.models.CartItem
import com.supplementstore.models.CreateOrderRequest
import com.supplementstore.models.Purchase
import com.supplementstore.models.ShippingAddress
import com.supplementstore.services.api.APIClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PurchaseViewModel : ViewModel() {
    private val _purchases = MutableStateFlow<List<Purchase>>(emptyList())
    val purchases: StateFlow<List<Purchase>> = _purchases.asStateFlow()

    init {
        loadPurchases()
    }

    // جلب الطلبات السابقة من السيرفر
    fun loadPurchases() {
        viewModelScope.launch {
            try {
                val response = APIClient.api.getOrders()
                if (response.isSuccessful && response.body() != null) {
                    _purchases.value = response.body()!!
                }
            } catch (e: Exception) {
                // ممكن تعالج الخطأ هنا
            }
        }
    }

    // إنشاء طلب (Order) جديد في السيرفر
    // خليناها suspend عشان شاشة الـ Checkout تستنى النتيجة قبل ما تنقل اليوزر
// إنشاء طلب (Order) جديد في السيرفر
    suspend fun addPurchase(
        items: List<CartItem>,
        total: Double,
        shippingAddress: ShippingAddress
    ): Boolean {
        return try {
            // دمجنا تفاصيل العنوان في نص واحد (String) عشان يطابق الموديل بتاعك
            val fullAddressString =
                "${shippingAddress.address}, ${shippingAddress.city}, ${shippingAddress.country} - ${shippingAddress.postalCode}"

            val request = CreateOrderRequest(
                shippingAddress = fullAddressString // دلوقتي بنبعت نص بدل Object
            )

            val response = APIClient.api.createOrder(request)

            if (response.isSuccessful) {
                loadPurchases() // بنحدث قائمة الطلبات
                true // الطلب نجح
            } else {
                false // الطلب فشل
            }
        } catch (e: Exception) {
            false
        }
    }

    val purchasesReversed: List<Purchase>
        get() = purchases.value.reversed()
}