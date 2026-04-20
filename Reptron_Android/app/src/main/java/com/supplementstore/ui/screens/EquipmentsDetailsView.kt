package com.supplementstore.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.supplementstore.models.Equipment
import com.supplementstore.models.Review
import com.supplementstore.navigation.AppRoute
import com.supplementstore.viewmodels.CartViewModel

@Composable
fun EquipmentsDetailsView(
    equipment: Equipment,
    cartViewModel: CartViewModel,
    onNavigate: (AppRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    var quantity by remember { mutableStateOf(1) }
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0F172A), Color(0xFF1E293B))
                )
            )
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(Color(0xFF1E293B)),
            contentAlignment = Alignment.Center
        ) {
            Text(equipment.name, color = Color.White, fontSize = 18.sp)
        }

        Column(
            modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = equipment.name,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$${String.format("%.2f", equipment.price)}",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Cyan
                    )

                    equipment.salePrice?.let { salePrice ->
                        Text(
                            text = "$${String.format("%.2f", salePrice)}",
                            fontSize = 20.sp,
                            color = Color.Red,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Quantity:", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.White)

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { if (quantity > 1) quantity-- }) {
                        Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = Color.Cyan)
                    }

                    Text("$quantity", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.width(40.dp))

                    IconButton(onClick = { quantity++ }) {
                        Icon(Icons.Default.Add, contentDescription = "Increase", tint = Color.Cyan)
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("Description", "Additional", "Reviews").forEachIndexed { index, title ->
                    TextButton(
                        onClick = { selectedTab = index },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = if (selectedTab == index) Color.Cyan else Color(0xFFCBD5E1)
                        )
                    ) {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            when (selectedTab) {
                0 -> Text(equipment.description, fontSize = 16.sp, color = Color(0xFFCBD5E1))
                1 -> Text(
                    equipment.additionalInfo ?: "No additional information available.",
                    fontSize = 16.sp,
                    color = Color(0xFFCBD5E1)
                )
                2 -> ReviewsSection(reviews = equipment.reviews)
            }

            Button(
                onClick = {
                    cartViewModel.addEquipmentToCart(equipment, quantity)
                    onNavigate(AppRoute.Cart)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(colors = listOf(Color.Cyan, Color(0xFF00BCD4))),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = Color(0xFF0F172A))
                        Text(
                            text = "Add to Cart",
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

@Composable
private fun ReviewsSection(reviews: List<Review>?) {
    if (reviews.isNullOrEmpty()) {
        Text("No reviews yet.", fontSize = 16.sp, color = Color(0xFFCBD5E1).copy(alpha = 0.7f))
    } else {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            reviews.forEach { review ->
                ReviewCard(review = review)
            }
        }
    }
}

@Composable
private fun ReviewCard(review: Review) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B).copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(review.name, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                Row {
                    repeat(5) { index ->
                        Text(
                            text = "★",
                            fontSize = 12.sp,
                            color = if (index < review.rating) Color.Yellow else Color.Gray
                        )
                    }
                }
            }

            Text(review.comment, fontSize = 14.sp, color = Color(0xFFCBD5E1))
        }
    }
}