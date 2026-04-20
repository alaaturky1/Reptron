package com.supplementstore.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.supplementstore.models.BillingInfo
import com.supplementstore.models.PaymentInfo
import com.supplementstore.models.ShippingAddress
import com.supplementstore.navigation.AppRoute
import com.supplementstore.viewmodels.CartViewModel
import com.supplementstore.viewmodels.PurchaseViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutView(
    cartViewModel: CartViewModel,
    purchaseViewModel: PurchaseViewModel,
    onNavigate: (AppRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    var billingInfo by remember { mutableStateOf(BillingInfo()) }
    var paymentInfo by remember { mutableStateOf(PaymentInfo()) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val cart by cartViewModel.cart.collectAsStateWithLifecycle()
    val grandTotal by remember { derivedStateOf { cartViewModel.grandTotal } }

    val isFormValid = billingInfo.name.isNotEmpty() &&
            billingInfo.email.isNotEmpty() &&
            billingInfo.address.isNotEmpty() &&
            billingInfo.city.isNotEmpty() &&
            paymentInfo.cardNumber.isNotEmpty() &&
            paymentInfo.cardName.isNotEmpty()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0F172A), Color(0xFF1E293B))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Checkout",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Cyan,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 32.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B).copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Billing Information",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    CheckoutTextField("Full Name", billingInfo.name) {
                        billingInfo = billingInfo.copy(name = it)
                    }
                    CheckoutTextField(
                        "Email",
                        billingInfo.email,
                        KeyboardType.Email
                    ) { billingInfo = billingInfo.copy(email = it) }
                    CheckoutTextField("Address", billingInfo.address) {
                        billingInfo = billingInfo.copy(address = it)
                    }
                    CheckoutTextField("City", billingInfo.city) {
                        billingInfo = billingInfo.copy(city = it)
                    }
                    CheckoutTextField("Postal Code", billingInfo.postalCode) {
                        billingInfo = billingInfo.copy(postalCode = it)
                    }
                    CheckoutTextField("Country", billingInfo.country) {
                        billingInfo = billingInfo.copy(country = it)
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B).copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Payment Information",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    CheckoutTextField("Card Number", paymentInfo.cardNumber, KeyboardType.Number) {
                        paymentInfo = paymentInfo.copy(cardNumber = it)
                    }
                    CheckoutTextField("Cardholder Name", paymentInfo.cardName) {
                        paymentInfo = paymentInfo.copy(cardName = it)
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        CheckoutTextField(
                            "Expiry (MM/YY)",
                            paymentInfo.expiry,
                            modifier = Modifier.weight(1f)
                        ) { paymentInfo = paymentInfo.copy(expiry = it) }
                        CheckoutTextField(
                            "CVV",
                            paymentInfo.cvv,
                            KeyboardType.Number,
                            modifier = Modifier.weight(1f)
                        ) { paymentInfo = paymentInfo.copy(cvv = it) }
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B).copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Order Summary",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    cart.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "${item.safeName} x${item.safeQuantity}",
                                fontSize = 14.sp,
                                color = Color(0xFFCBD5E1)
                            )
                            Text(
                                "$${String.format("%.2f", item.safePrice * item.safeQuantity)}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }
                    Divider(color = Color.Cyan.copy(alpha = 0.3f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Total:",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            "$${String.format("%.2f", grandTotal)}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Cyan
                        )
                    }
                }
            }

            Button(
                onClick = {
                    val shippingAddress = ShippingAddress(
                        name = billingInfo.name,
                        address = billingInfo.address,
                        city = billingInfo.city,
                        postalCode = billingInfo.postalCode,
                        country = billingInfo.country
                    )

                    scope.launch {
                        isLoading = true

                        val isSuccess =
                            purchaseViewModel.addPurchase(cart, grandTotal, shippingAddress)

                        if (isSuccess) {
                            cartViewModel.clearCart()
                            onNavigate(AppRoute.MyPurchases)
                        }

                        isLoading = false
                    }
                },
                enabled = isFormValid && !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Cyan,
                                    Color(0xFF00BCD4)
                                )
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color(0xFF0F172A),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Place Order",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF0F172A)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CheckoutTextField(
    title: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    "Enter ${title.lowercase()}",
                    color = Color(0xFFCBD5E1).copy(alpha = 0.7f)
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = ImeAction.Next
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF1E293B).copy(alpha = 0.8f),
                unfocusedContainerColor = Color(0xFF1E293B).copy(alpha = 0.8f),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color.Cyan.copy(alpha = 0.3f),
                unfocusedBorderColor = Color.Cyan.copy(alpha = 0.3f)
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )
    }
}