package com.supplementstore.ui.screens

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.supplementstore.models.CartItem
import com.supplementstore.navigation.AppRoute
import com.supplementstore.viewmodels.CartViewModel

@Composable
fun CartView(
    cartViewModel: CartViewModel,
    onNavigate: (AppRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        cartViewModel.fetchCart()
    }
    val cart by cartViewModel.cart.collectAsStateWithLifecycle()
    val grandTotal = cartViewModel.grandTotal

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0F172A), Color(0xFF1E293B))
                )
            )
    ) {
        if (cart.isEmpty()) {
            EmptyCartView(onNavigate = onNavigate)
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 120.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "Shopping Cart",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Cyan,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 32.dp)
                    )
                }

                items(cart, key = { it.id }) { item ->
                    CartItemRow(
                        item = item,
                        onIncreaseQuantity = {
                            cartViewModel.increaseQuantity(
                                item.id,
                                item.safeQuantity
                            )
                        },
                        onDecreaseQuantity = {
                            cartViewModel.decreaseQuantity(
                                item.id,
                                item.safeQuantity
                            )
                        },
                        onRemove = { cartViewModel.removeFromCart(item.id) },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                item {
                    TotalSection(
                        total = grandTotal,
                        onNavigate = onNavigate,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CartItemRow(
    item: CartItem,
    onIncreaseQuantity: () -> Unit,
    onDecreaseQuantity: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B).copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            coil.compose.AsyncImage(
                model = item.safeImg,
                contentDescription = item.safeName,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White),
                contentScale = androidx.compose.ui.layout.ContentScale.Fit
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = item.safeName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1
                )
                Text(
                    text = "$${String.format("%.2f", item.safePrice)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Cyan
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    IconButton(
                        onClick = onDecreaseQuantity,
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color(0xFF0F172A), RoundedCornerShape(8.dp))
                    ) {
                        Icon(
                            Icons.Default.Remove,
                            "Less",
                            tint = Color.Cyan,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        "${item.safeQuantity}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(
                        onClick = onIncreaseQuantity,
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color(0xFF0F172A), RoundedCornerShape(8.dp))
                    ) {
                        Icon(
                            Icons.Default.Add,
                            "More",
                            tint = Color.Cyan,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
            IconButton(onClick = onRemove) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Remove",
                    tint = Color.Red.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun TotalSection(
    total: Double,
    onNavigate: (AppRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B).copy(alpha = 0.8f))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Total Amount", color = Color.White, fontSize = 18.sp)
                Text(
                    "$${String.format("%.2f", total)}",
                    color = Color.Cyan,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Button(
                onClick = { onNavigate(AppRoute.Checkout) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp)
            ) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(listOf(Color.Cyan, Color(0xFF00BCD4))),
                            RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Proceed to Checkout",
                        color = Color(0xFF0F172A),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyCartView(onNavigate: (AppRoute) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.ShoppingCart, null, modifier = Modifier.size(80.dp), tint = Color.Gray)
        Spacer(Modifier.height(16.dp))
        Text(
            "Your cart is empty",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = { onNavigate(AppRoute.Store) },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Cyan)
        ) {
            Text("Shop Now", color = Color.Black)
        }
    }
}