package com.supplementstore.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.supplementstore.navigation.AppRoute
import com.supplementstore.viewmodels.UserViewModel

@Composable
fun ProfileView(
    userViewModel: UserViewModel,
    onNavigate: (AppRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    val userToken by userViewModel.userToken.collectAsStateWithLifecycle(initialValue = null)
    val userName by userViewModel.userName.collectAsStateWithLifecycle(initialValue = "")
    val isLoggedIn = userToken != null

    val bgColor = Color(0xFF040A18)
    val cardBgColor = Color(0xFF0A1424)
    val innerBoxColor = Color(0xFF060D1A)
    val cardBorderColor = Color(0xFF14243B)
    val cyanAccent = Color(0xFF00D4FF)
    val textGray = Color(0xFF8293A8)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp, bottom = 120.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Card(
                modifier = Modifier.fillMaxWidth(0.85f),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = cardBgColor),
                border = BorderStroke(1.dp, cardBorderColor)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AccountCircle, contentDescription = null, tint = cyanAccent, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("User Info", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(80.dp), contentAlignment = Alignment.Center) {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .border(1.5.dp, cyanAccent.copy(alpha = 0.5f), CircleShape)
                            )
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .background(cyanAccent.copy(alpha = 0.1f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = cyanAccent, modifier = Modifier.size(40.dp))
                            }
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .offset(x = 4.dp, y = 4.dp)
                                    .size(28.dp)
                                    .background(cyanAccent, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.CameraAlt, contentDescription = null, tint = bgColor, modifier = Modifier.size(16.dp))
                            }
                        }

                        Spacer(modifier = Modifier.width(24.dp))

                        Column {
                            Text("Name", color = textGray, fontSize = 12.sp)
                            Text(if (userName.isNullOrEmpty()) "Not set" else userName!!, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("Email", color = textGray, fontSize = 12.sp)
                            Text("Not set", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(if (isLoggedIn) "Logged In" else "Logged Out", color = cyanAccent, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedButton(
                        onClick = { },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, cardBorderColor),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent, contentColor = Color.White)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Edit Profile", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            SectionCard(
                title = "Orders",
                icon = Icons.Outlined.Inventory2,
                emptyText = "No orders yet",
                cardBgColor = cardBgColor,
                innerBoxColor = innerBoxColor,
                cardBorderColor = cardBorderColor,
                cyanAccent = cyanAccent,
                textGray = textGray
            )

            Spacer(modifier = Modifier.height(16.dp))

            SectionCard(
                title = "Payments",
                icon = Icons.Default.CreditCard,
                emptyText = "No completed payments",
                cardBgColor = cardBgColor,
                innerBoxColor = innerBoxColor,
                cardBorderColor = cardBorderColor,
                cyanAccent = cyanAccent,
                textGray = textGray
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onNavigate(AppRoute.MyPurchases) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(
                        elevation = 20.dp,
                        shape = RoundedCornerShape(16.dp),
                        spotColor = cyanAccent,
                        ambientColor = cyanAccent
                    ),
                colors = ButtonDefaults.buttonColors(containerColor = cyanAccent),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.History, contentDescription = null, tint = bgColor)
                Spacer(modifier = Modifier.width(8.dp))
                Text("View Full Purchase History", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = bgColor)
            }

            Spacer(modifier = Modifier.height(24.dp))

            SecondaryButton(
                text = "Change Password",
                icon = Icons.Default.VpnKey,
                onClick = { },
                cardBgColor = cardBgColor,
                cardBorderColor = cardBorderColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            SecondaryButton(
                text = "Logout",
                icon = Icons.Default.Logout,
                onClick = {
                    userViewModel.logout()
                    onNavigate(AppRoute.Home)
                },
                cardBgColor = cardBgColor,
                cardBorderColor = cardBorderColor
            )
        }
    }
}

@Composable
fun SectionCard(
    title: String,
    icon: ImageVector,
    emptyText: String,
    cardBgColor: Color,
    innerBoxColor: Color,
    cardBorderColor: Color,
    cyanAccent: Color,
    textGray: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardBgColor),
        border = BorderStroke(1.dp, cardBorderColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = cyanAccent, modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(title, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(innerBoxColor, RoundedCornerShape(12.dp))
                    .border(1.dp, cardBorderColor, RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Text(emptyText, color = textGray, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun SecondaryButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    cardBgColor: Color,
    cardBorderColor: Color
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(0.55f)
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(containerColor = cardBgColor),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, cardBorderColor)
    ) {
        Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}