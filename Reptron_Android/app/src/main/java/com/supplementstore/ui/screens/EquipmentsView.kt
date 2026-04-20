package com.supplementstore.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.supplementstore.models.Equipment
import com.supplementstore.navigation.AppRoute
import com.supplementstore.viewmodels.EquipmentsViewModel

@Composable
fun EquipmentsView(
    equipmentsViewModel: EquipmentsViewModel = viewModel(),
    onNavigate: (AppRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    val equipments by equipmentsViewModel.equipments.collectAsStateWithLifecycle()
    var activeFilter by remember { mutableStateOf("all") }

    val filteredEquipments = remember(equipments, activeFilter) {
        if (activeFilter == "all") {
            equipments
        } else {
            equipments.filter {
                it.specialty.equals(activeFilter, ignoreCase = true)
            }
        }
    }

    val specialties = remember(equipments) {
        buildList {
            add("all")
            addAll(equipments.map { it.specialty }.distinct())
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0F172A), Color(0xFF1E293B))
                )
            )
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 120.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = "Equipment",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Cyan,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(specialties) { specialty ->
                        FilterButton(
                            text = specialty.replaceFirstChar { it.uppercase() },
                            isSelected = activeFilter == specialty,
                            onClick = { activeFilter = specialty }
                        )
                    }
                }
            }

            if (filteredEquipments.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 64.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No equipment found",
                            fontSize = 18.sp,
                            color = Color(0xFFCBD5E1)
                        )
                    }
                }
            } else {
                items(filteredEquipments) { equipment ->
                    EquipmentCard(
                        equipment = equipment,
                        onClick = {
                            onNavigate(AppRoute.EquipmentDetails(equipment.id))
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color.Transparent else Color(0xFF1E293B)
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    if (isSelected) {
                        Brush.horizontalGradient(colors = listOf(Color.Cyan, Color(0xFF00BCD4)))
                    } else {
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF1E293B),
                                Color(0xFF0F172A)
                            )
                        )
                    },
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isSelected) Color(0xFF0F172A) else Color.White
            )
        }
    }
}

@Composable
private fun EquipmentCard(
    equipment: Equipment,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B).copy(alpha = 0.8f))
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(Color(0xFF1E293B)),
                contentAlignment = Alignment.Center
            ) {
                Text(equipment.name, color = Color.White, fontSize = 14.sp)
            }

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = equipment.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 2
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$${String.format("%.2f", equipment.price)}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Cyan
                    )

                    equipment.salePrice?.let { salePrice ->
                        Text(
                            text = "$${String.format("%.2f", salePrice)}",
                            fontSize = 14.sp,
                            color = Color.Red,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }
            }
        }
    }
}