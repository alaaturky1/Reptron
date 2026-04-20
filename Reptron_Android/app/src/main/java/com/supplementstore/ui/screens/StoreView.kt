package com.supplementstore.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.supplementstore.models.Equipment
import com.supplementstore.navigation.AppRoute
import com.supplementstore.ui.components.ProductCard
import com.supplementstore.ui.components.ProductCardStoreProduct
import com.supplementstore.viewmodels.EquipmentsViewModel
import com.supplementstore.viewmodels.ProductState
import com.supplementstore.viewmodels.StoreViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun StoreView(
    storeViewModel: StoreViewModel,
    equipmentsViewModel: EquipmentsViewModel,
    onNavigate: (AppRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by storeViewModel.productState.collectAsStateWithLifecycle()
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0F172A), Color(0xFF1E293B), Color(0xFF0F172A))
                )
            )
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .offset(x = 100.dp, y = (-50).dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color.Cyan.copy(alpha = 0.15f), Color.Transparent)
                    ),
                    shape = CircleShape
                )
                .blur(80.dp)
                .zIndex(0f)
        )

        Column(modifier = Modifier.fillMaxSize()) {
            HeaderSection()

            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.Transparent,
                contentColor = Color.Cyan,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = Color.Cyan,
                        height = 3.dp
                    )
                },
                divider = {}
            ) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = { Text("Supplements", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
                    selectedContentColor = Color.Cyan,
                    unselectedContentColor = Color.Gray
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = { Text("Equipments", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
                    selectedContentColor = Color.Cyan,
                    unselectedContentColor = Color.Gray
                )
            }

            if (selectedTabIndex == 0) {
                when (val currentState = state) {
                    is ProductState.Loading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = Color.Cyan)
                        }
                    }
                    is ProductState.Error -> {
                        Column(
                            modifier = Modifier.fillMaxSize().padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = currentState.message, color = Color.Red, textAlign = TextAlign.Center)
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { storeViewModel.loadProducts() },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Cyan)
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null, tint = Color(0xFF0F172A))
                                Text("Retry", color = Color(0xFF0F172A))
                            }
                        }
                    }
                    is ProductState.Success -> {
                        val products = currentState.products
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = 80.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            if (products.isEmpty()) {
                                item { EmptyStorePlaceholder() }
                            } else {
                                items(products.size) { index ->
                                    val product = products[index]
                                    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                                        ProductCard(
                                            product = ProductCardStoreProduct.from(product),
                                            onTap = { onNavigate(AppRoute.ProductDetails(product.id)) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                val equipments = equipmentsViewModel.equipments.value
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 80.dp, top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (equipments.isEmpty()) {
                        item { EmptyStorePlaceholder() }
                    } else {
                        items(equipments.size) { index ->
                            val equipment = equipments[index]
                            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                                EquipmentItemCard(equipment = equipment) {
                                    onNavigate(AppRoute.EquipmentDetails(equipment.id))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EquipmentItemCard(equipment: Equipment, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B).copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color(0xFF0F172A), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = Color.Cyan, modifier = Modifier.size(32.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = equipment.name, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(text = equipment.specialty, color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "$${equipment.price}", color = Color.Cyan, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun HeaderSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Welcome to our\nStore",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Cyan,
            textAlign = TextAlign.Center,
            lineHeight = 36.sp
        )
        Text(
            text = "Premium supplements & equipments",
            fontSize = 16.sp,
            color = Color(0xFFCBD5E1).copy(alpha = 0.9f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun EmptyStorePlaceholder() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Inbox,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = Color.Cyan
        )
        Text(
            text = "No Items Available",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Cyan
        )
        Text(
            text = "We're currently updating our inventory. Please check back soon!",
            fontSize = 16.sp,
            color = Color(0xFFCBD5E1),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}