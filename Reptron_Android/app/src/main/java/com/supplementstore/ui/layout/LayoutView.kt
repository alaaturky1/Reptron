    package com.supplementstore.ui.layout

    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.layout.*
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.*
    import androidx.compose.material3.*
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.collectAsState
    import androidx.compose.runtime.getValue
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.graphics.vector.ImageVector
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import com.supplementstore.navigation.AppRoute
    import com.supplementstore.viewmodels.CartViewModel
    import com.supplementstore.viewmodels.UserViewModel

    @Composable
    fun LayoutView(
        content: @Composable () -> Unit,
        userViewModel: UserViewModel,
        cartViewModel: CartViewModel,
        onNavigate: (AppRoute) -> Unit,
        modifier: Modifier = Modifier
    ) {
        Column(modifier = modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                content()
            }
            BottomNavbarView(
                userViewModel = userViewModel,
                cartViewModel = cartViewModel,
                onNavigate = onNavigate
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun BottomNavbarView(
        userViewModel: UserViewModel,
        cartViewModel: CartViewModel,
        onNavigate: (AppRoute) -> Unit
    ) {
        val userToken by userViewModel.userToken.collectAsState(initial = null)
        val isLoggedIn = userToken != null
        val itemsCount by cartViewModel.itemsCount.collectAsState()

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFF0F172A).copy(alpha = 0.95f),
            shadowElevation = 8.dp
        ) {
            Column {
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Cyan.copy(alpha = 0.25f),
                    thickness = 1.dp
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isLoggedIn) {
                        BottomBarButton("Home", Icons.Default.Home) { onNavigate(AppRoute.Home) }
                        BottomBarButton("Store", Icons.Default.Store) { onNavigate(AppRoute.Store) }
                        // تم مسح زرار Equipments من هنا لتوسيع المساحة
                        BottomBarButton("Coaches", Icons.Default.People) { onNavigate(AppRoute.Coaches) }
                        BottomBarButton("AI", Icons.Default.AutoAwesome) { onNavigate(AppRoute.AiCoach) }
                        Box {
                            BottomBarButton("Cart", Icons.Default.ShoppingCart) { onNavigate(AppRoute.Cart) }
                            if (itemsCount > 0) {
                                Badge(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .offset(x = (0).dp, y = (-4).dp)
                                ) {
                                    Text(text = "$itemsCount", fontSize = 10.sp, color = Color.White)
                                }
                            }
                        }
                        BottomBarButton("Profile", Icons.Default.Person) { onNavigate(AppRoute.Profile) }
                    } else {
                        BottomBarButton("Home", Icons.Default.Home) { onNavigate(AppRoute.Home) }
                        BottomBarButton("Login", Icons.Default.AccountCircle) { onNavigate(AppRoute.Login) }
                        BottomBarButton("Sign Up", Icons.Default.PersonAdd) { onNavigate(AppRoute.Register) }
                    }
                }
            }
        }
    }
    @Composable
    private fun BottomBarButton(
        title: String,
        icon: ImageVector,
        onClick: () -> Unit
    ) {
        Column(
            modifier = Modifier
                .wrapContentWidth()
                .clickable { onClick() }
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
