package com.supplementstore.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.supplementstore.navigation.AppRoute

@Composable
fun FooterView(
    onNavigate: (AppRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF0F172A).copy(alpha = 0.8f))
    ) {
        // Top border
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color.Cyan, Color(0xFF00BCD4))
                    )
                )
        )
        
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Quick Links Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Quick Links",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Cyan
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    FooterLink("Home", onClick = { onNavigate(AppRoute.Home) })
                    FooterLink("Store", onClick = { onNavigate(AppRoute.Store) })
                    FooterLink("About Us", onClick = { onNavigate(AppRoute.AboutUs) })
                }
            }
            
            // Categories Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Categories",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Cyan
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    FooterLink("Supplements", onClick = { onNavigate(AppRoute.Store) })
                    FooterLink("Equipment", onClick = { onNavigate(AppRoute.Equipments) })
                    FooterLink("Coaches", onClick = { onNavigate(AppRoute.Coaches) })
                }
            }
            
            // Newsletter Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Newsletter",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Cyan
                )
                
                Text(
                    text = "Subscribe to get updates on new products and offers",
                    fontSize = 14.sp,
                    color = Color(0xFFCBD5E1),
                    textAlign = TextAlign.Center
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier.weight(1f),
                        placeholder = {
                            Text(
                                text = "Enter your email",
                                color = Color(0xFFCBD5E1).copy(alpha = 0.7f)
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Send
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF1E293B).copy(alpha = 0.8f),
                            unfocusedContainerColor = Color(0xFF1E293B).copy(alpha = 0.8f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color.Cyan.copy(alpha = 0.3f),
                            unfocusedBorderColor = Color.Cyan.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )
                    
                    Button(
                        onClick = {
                            // TODO: Implement newsletter signup
                            email = ""
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(Color.Cyan, Color(0xFF00BCD4))
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = "Subscribe",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF0F172A)
                        )
                    }
                }
            }
            
            // Copyright
            Text(
                text = "© 2025 Supplement Store. All rights reserved.",
                fontSize = 12.sp,
                color = Color(0xFFCBD5E1).copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun FooterLink(
    title: String,
    onClick: () -> Unit
) {
    Text(
        text = title,
        fontSize = 14.sp,
        color = Color(0xFFCBD5E1),
        modifier = Modifier.clickable(onClick = onClick)
    )
}
