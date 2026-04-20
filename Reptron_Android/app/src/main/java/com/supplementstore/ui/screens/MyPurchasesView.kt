package com.supplementstore.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.supplementstore.models.Purchase
import com.supplementstore.viewmodels.PurchaseViewModel

@Composable
fun MyPurchasesView(
    purchaseViewModel: PurchaseViewModel,
    modifier: Modifier = Modifier
) {
    val purchases by purchaseViewModel.purchases.collectAsStateWithLifecycle()
    val purchasesReversed = purchases.reversed()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0F172A), Color(0xFF1E293B))
                )
            )
    ) {
        if (purchasesReversed.isEmpty()) {
            EmptyPurchasesView()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    Text(
                        text = "My Purchases",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Cyan,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 32.dp)
                    )
                }

                items(purchasesReversed, key = { it.id }) { purchase ->
                    PurchaseCard(
                        purchase = purchase,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(200.dp))
                }
            }
        }
    }
}

@Composable
private fun EmptyPurchasesView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.ShoppingBag,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = Color.Cyan.copy(alpha = 0.5f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "No purchases yet",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Your order history will appear here",
            fontSize = 16.sp,
            color = Color(0xFFCBD5E1)
        )
    }
}

@SuppressLint("DefaultLocale")
@Composable
private fun PurchaseCard(
    purchase: Purchase,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B).copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Order Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Order #${purchase.id}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = purchase.date,
                        fontSize = 14.sp,
                        color = Color(0xFFCBD5E1)
                    )
                }

                Text(
                    text = "$${String.format("%.2f", purchase.total)}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Cyan
                )
            }

            Divider(color = Color.Cyan.copy(alpha = 0.3f))

            // Items
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                purchase.items.forEach { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${item.safeName} x${item.safeQuantity}",
                            fontSize = 14.sp,
                            color = Color(0xFFCBD5E1)
                        )
                        Text(
                            text = "$${
                                String.format(
                                    "%.2f",
                                    item.safePrice * item.safeQuantity
                                )
                            }",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Shipping to:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFCBD5E1)
                )

                Text(
                    text = "${purchase.shippingAddress.name}\n${purchase.shippingAddress.address}\n${purchase.shippingAddress.city}, ${purchase.shippingAddress.postalCode}\n${purchase.shippingAddress.country}",
                    fontSize = 12.sp,
                    color = Color(0xFFCBD5E1).copy(alpha = 0.8f)
                )
            }
        }
    }
}
