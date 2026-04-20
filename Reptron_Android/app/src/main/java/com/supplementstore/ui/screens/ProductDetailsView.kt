package com.supplementstore.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.supplementstore.models.Product
import com.supplementstore.models.Review
import com.supplementstore.navigation.AppRoute
import com.supplementstore.viewmodels.CartViewModel

@Composable
fun ProductDetailsView(
    product: Product,
    cartViewModel: CartViewModel,
    onNavigate: (AppRoute) -> Unit
) {
    var quantity by remember { mutableIntStateOf(1) }
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        containerColor = Color(0xFF0F172A),
        bottomBar = {
            // زرار الإضافة للسلة
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF0F172A),
                shadowElevation = 8.dp
            ) {
                Button(
                    onClick = {
                        // هنا بننادي على الـ ViewModel عشان يضيف للسيرفر
                        cartViewModel.addProductToCart(product, quantity)
                        onNavigate(AppRoute.Cart)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        Color(0xFF22D3EE),
                                        Color(0xFF06B6D4)
                                    )
                                ),
                                RoundedCornerShape(14.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.ShoppingCart, null, tint = Color(0xFF0F172A))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Add to Cart",
                                color = Color(0xFF0F172A),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // منطقة الصورة (Dynamic Image)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .background(Color.White)
            ) {
                AsyncImage(
                    model = product.image, // الرابط اللي جاي من الـ API
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentScale = ContentScale.Fit
                )

                IconButton(
                    onClick = { onNavigate(AppRoute.Store) },
                    modifier = Modifier
                        .padding(16.dp)
                        .size(40.dp)
                        .background(Color(0xFF0F172A).copy(alpha = 0.7f), CircleShape)
                ) {
                    Icon(
                        Icons.Default.ArrowBackIosNew,
                        null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // تفاصيل المنتج
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    product.name,
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "$${String.format("%.2f", product.price)}",
                        color = Color(0xFF22D3EE),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    product.oldPrice?.let {
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "$${String.format("%.2f", it)}",
                            color = Color.Red,
                            fontSize = 18.sp,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // اختيار الكمية
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Quantity:", color = Color.White, fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(16.dp))
                    QuantitySelector(
                        quantity = quantity,
                        onIncrease = { quantity++ },
                        onDecrease = { if (quantity > 1) quantity-- }
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // التبويبات (Tabs)
                val tabs = listOf("Description", "Reviews")
                Row(modifier = Modifier.fillMaxWidth()) {
                    tabs.forEachIndexed { index, title ->
                        TabItem(
                            title = title,
                            isSelected = selectedTab == index,
                            onClick = { selectedTab = index },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Divider(color = Color(0xFF334155), thickness = 1.dp)
                Spacer(modifier = Modifier.height(16.dp))

                // محتوى التبويب المختار
                when (selectedTab) {
                    0 -> Text(product.description, color = Color(0xFFCBD5E1), lineHeight = 22.sp)
                    1 -> ReviewsSection(product.reviews)
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun QuantitySelector(quantity: Int, onIncrease: () -> Unit, onDecrease: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(
            onClick = onDecrease,
            modifier = Modifier
                .background(Color(0xFF1E293B), CircleShape)
                .size(32.dp)
        ) {
            Icon(Icons.Default.Remove, null, tint = Color.Cyan, modifier = Modifier.size(18.dp))
        }
        Text(
            "$quantity",
            color = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp),
            fontSize = 18.sp
        )
        IconButton(
            onClick = onIncrease,
            modifier = Modifier
                .background(Color(0xFF1E293B), CircleShape)
                .size(32.dp)
        ) {
            Icon(Icons.Default.Add, null, tint = Color.Cyan, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
fun TabItem(title: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.clickable { onClick() }) {
        Text(
            title,
            color = if (isSelected) Color.Cyan else Color.Gray,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        if (isSelected) Box(Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(Color.Cyan))
    }
}

@Composable
private fun ReviewsSection(reviews: List<Review>?) {
    if (reviews.isNullOrEmpty()) {
        Text(
            "No reviews yet. Be the first to review!",
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    } else {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            reviews.forEach { review ->
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(review.name, color = Color.White, fontWeight = FontWeight.Bold)
                            Row {
                                repeat(5) { i ->
                                    Text(
                                        "★",
                                        color = if (i < review.rating) Color.Yellow else Color.Gray
                                    )
                                }
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(review.comment, color = Color(0xFFCBD5E1), fontSize = 14.sp)
                    }
                }
            }
        }
    }
}