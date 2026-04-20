package com.supplementstore.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BlogCard(
    image: String,
    title: String,
    date: String,
    onTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isPressed) 0.98f else 1.0f)
    val alpha by animateFloatAsState(if (isPressed) 0.9f else 1.0f)
    
    Card(
        modifier = modifier
            .width(300.dp)
            .scale(scale)
            .clickable(onClick = onTap),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E293B).copy(alpha = 0.8f * alpha)
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color(0xFF1E293B))
            ) {
                // Note: Load image from resources
                Text(
                    text = title,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 14.sp
                )
            }
            
            // Body
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 2
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = date,
                        fontSize = 14.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }
        }
        
        // Top border gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.Cyan,
                            Color(0xFF00BCD4),
                            Color(0xFF0097A7)
                        )
                    )
                )
        )
    }
}
