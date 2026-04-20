package com.supplementstore.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.supplementstore.navigation.AppRoute

@Composable
fun NotFoundView(
    onNavigate: (AppRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0F172A), Color(0xFF1E293B))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = Color.Cyan.copy(alpha = 0.5f)
            )
            
            Text(
                text = "404",
                fontSize = 72.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Cyan
            )
            
            Text(
                text = "Page Not Found",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Text(
                text = "The page you're looking for doesn't exist.",
                fontSize = 16.sp,
                color = Color(0xFFCBD5E1),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = { onNavigate(AppRoute.Home) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = MaterialTheme.shapes.medium
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            Brush.horizontalGradient(colors = listOf(Color.Cyan, Color(0xFF00BCD4))),
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(horizontal = 32.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = "Go Home",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF0F172A)
                    )
                }
            }
        }
    }
}
