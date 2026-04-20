package com.supplementstore.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.supplementstore.models.Coach
import com.supplementstore.navigation.AppRoute
import com.supplementstore.viewmodels.CoachesViewModel

@Composable
fun CoachesView(
    coachesViewModel: CoachesViewModel = viewModel(),
    onNavigate: (AppRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    val coaches by coachesViewModel.coaches.collectAsStateWithLifecycle()
    var activeFilter by remember { mutableStateOf("all") }

    val filteredCoaches = remember(coaches, activeFilter) {
        if (activeFilter == "all") {
            coaches
        } else {
            coaches.filter {
                it.specialty.equals(activeFilter, ignoreCase = true)
            }
        }
    }

    val specialties = remember(coaches) {
        buildList {
            add("all")
            addAll(coaches.map { it.specialty }.distinct())
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
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 80.dp // ضفت المساحة دي من تحت عشان الـ Bottom Nav ما يغطيش على آخر كارت
            ),
            modifier = Modifier.fillMaxSize()
        ) {

            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = "Our Coaches",
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

            if (filteredCoaches.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Text(
                        text = "No coaches found",
                        fontSize = 18.sp,
                        color = Color(0xFFCBD5E1),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 64.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            } else {
                items(filteredCoaches) { coach ->
                    CoachCard(
                        coach = coach,
                        onClick = { onNavigate(AppRoute.Coach(coach.id)) }
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
private fun CoachCard(
    coach: Coach,
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
                Text(coach.name, color = Color.White, fontSize = 14.sp)
            }

            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = coach.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1
                )

                Text(
                    text = coach.specialty,
                    fontSize = 14.sp,
                    color = Color.Cyan,
                    maxLines = 1
                )

                Text(
                    text = coach.bio,
                    fontSize = 12.sp,
                    color = Color(0xFFCBD5E1),
                    maxLines = 2
                )
            }
        }
    }
}