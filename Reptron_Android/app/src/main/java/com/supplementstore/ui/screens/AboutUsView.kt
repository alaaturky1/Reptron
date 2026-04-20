package com.supplementstore.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.supplementstore.ui.components.FooterView

@Composable
fun AboutUsView(
    modifier: Modifier = Modifier
) {
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
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Text(
                text = "About Us",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Cyan,
                modifier = Modifier.padding(top = 32.dp)
            )
            
            InfoSection(
                title = "Our Mission",
                content = "To provide premium supplements, elite equipment, and expert guidance to help you achieve your fitness goals and transform your health."
            )
            
            InfoSection(
                title = "Our Vision",
                content = "To be the leading destination for fitness enthusiasts, athletes, and health-conscious individuals seeking quality products and expert support."
            )
            
            InfoSection(
                title = "Our Values",
                content = "Quality, Integrity, Innovation, and Customer Satisfaction are at the core of everything we do."
            )
            
            FooterView(onNavigate = { }, modifier = Modifier.padding(vertical = 64.dp))
        }
    }
}

@Composable
private fun InfoSection(
    title: String,
    content: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B).copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Text(
                text = content,
                fontSize = 16.sp,
                color = Color(0xFFCBD5E1)
            )
        }
    }
}
