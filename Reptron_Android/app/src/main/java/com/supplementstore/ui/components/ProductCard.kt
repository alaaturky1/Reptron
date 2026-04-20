package com.supplementstore.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun ProductCard(
    product: ProductCardStoreProduct,
    onTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isPressed) 0.98f else 1.0f, label = "")

    Card(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable { onTap() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E293B).copy(alpha = 0.8f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 20.dp,
            pressedElevation = 10.dp
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // Cyan Top Border
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color.Cyan, Color(0xFF00BCD4), Color(0xFF0097A7))
                        )
                    )
            )

            // Image Area (Dynamic via Coil)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(Color.White)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                // ✅ AsyncImage هتاخد الرابط اللي جاي من الـ API وتعرضه
                AsyncImage(
                    model = product.imageName,
                    contentDescription = product.name,
                    modifier = Modifier
                        .size(180.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Fit
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = product.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$${String.format("%.2f", product.price)}",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Cyan
                    )
                    product.oldPrice?.let { oldPrice ->
                        Text(
                            text = "$${String.format("%.2f", oldPrice)}",
                            fontSize = 19.sp,
                            color = Color(0xFFEF4444),
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }

                // View Details Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color.Cyan, Color(0xFF00BCD4))
                            )
                        )
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    isPressed = true
                                    tryAwaitRelease()
                                    isPressed = false
                                },
                                onTap = { onTap() }
                            )
                        }
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = null,
                            tint = Color(0xFF0F172A),
                            modifier = Modifier.size(19.dp)
                        )
                        Text(
                            text = "View Details",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF0F172A),
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }
    }
}

// الـ Data Class والمحول (Companion Object) بيفضلوا زي ما هم
data class ProductCardStoreProduct(
    val id: Int,
    val imageName: String,
    val name: String,
    val price: Double,
    val oldPrice: Double?
) {
    companion object {
        fun from(product: com.supplementstore.models.Product): ProductCardStoreProduct {
            return ProductCardStoreProduct(
                id = product.id,
                // لو السيرفر بعت null، هنعوض عنها بنص فاضي عشان نمنع الـ Crash
                imageName = product.img ?: "",
                name = product.name ?: "Unknown Product",
                price = product.price ?: 0.0,
                oldPrice = product.oldPrice
            )
        }

        fun from(product: com.supplementstore.models.StoreProduct): ProductCardStoreProduct {
            return ProductCardStoreProduct(
                id = product.id,
                imageName = product.img ?: "",
                name = product.name ?: "Unknown Product",
                price = product.price ?: 0.0,
                oldPrice = product.oldPrice
            )
        }

        fun from(product: com.supplementstore.models.HomeProduct): ProductCardStoreProduct {
            return ProductCardStoreProduct(
                id = product.id,
                imageName = product.image ?: "",
                name = product.name ?: "Unknown Product",
                price = product.price ?: 0.0,
                oldPrice = product.originalPrice
            )
        }
    }

}