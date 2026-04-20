package com.supplementstore.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.supplementstore.navigation.AppRoute
import com.supplementstore.ui.components.*
import com.supplementstore.viewmodels.CartViewModel
import com.supplementstore.viewmodels.HomeViewModel
import com.supplementstore.viewmodels.UserViewModel
import kotlinx.coroutines.delay

data class Category(
    val id: String,
    val name: String,
    val icon: String
)

data class Feature(
    val id: Int,
    val icon: String,
    val title: String,
    val description: String
)

data class BlogPost(
    val id: Int,
    val image: String,
    val title: String,
    val date: String
)

@Composable
fun HomeView(
    userViewModel: UserViewModel,
    cartViewModel: CartViewModel,
    homeViewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onNavigate: (AppRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    val bestSellers by homeViewModel.bestSellers.collectAsState()
    val isLoading by homeViewModel.isLoading.collectAsState()

    var searchTerm by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("all") }
    var activeTestimonial by remember { mutableStateOf(0) }

    val userToken by userViewModel.userToken.collectAsState(initial = null)
    val isLoggedIn = userToken != null
    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600

    val testimonials = remember {
        listOf(
            Testimonial(1, "Alex Rodriguez", "Professional Athlete", "Supplement Store transformed my recovery and boosted performance.", "testimonial-2"),
            Testimonial(2, "Sarah Chen", "Fitness Coach", "The equipment quality and durability are unmatched!", "testimonial-4")
        )
    }

    val categories = remember {
        listOf(
            Category("all", "All Products", "bolt.fill"),
            Category("supplements", "Supplements", "pills.fill"),
            Category("equipment", "Equipment", "dumbbell.fill")
        )
    }

    val features = remember {
        listOf(
            Feature(1, "star.fill", "Premium Quality", "Lab-tested ingredients"),
            Feature(2, "trophy.fill", "Trusted by Athletes", "Used worldwide"),
            Feature(3, "shippingbox.fill", "Fast Shipping", "Free delivery over $50"),
            Feature(4, "shield.fill", "Money Back Guarantee", "30 days return")
        )
    }

    val workoutPrograms = remember {
        listOf(
            WorkoutProgram(1, "Strength Training", "Strength Training", "Build maximum muscle and increase explosive power."),
            WorkoutProgram(2, "Fat Loss", "Fat Loss", "Burn calories fast with structured HIIT and cardio workouts."),
            WorkoutProgram(3, "Endurance", "Endurance", "Is the ability of an organism to exert itself and remain active for a long period of time.")
        )
    }

    val blogPosts = remember {
        listOf(
            BlogPost(1, "Nutrition", "Top 10 Healthiest Foods for Muscle Growth", "Feb 2025"),
            BlogPost(2, "Supplements", "Do You Really Need Creatine?", "Jan 2025"),
            BlogPost(3, "Workout", "5 Best HIIT Routines for Fat Burn", "March 2025")
        )
    }

    val filteredProducts = remember(bestSellers, searchTerm, selectedCategory) {
        bestSellers.filter { product ->
            val matchSearch = searchTerm.isEmpty() ||
                    product.name.contains(searchTerm, ignoreCase = true) ||
                    (product.description?.contains(searchTerm, ignoreCase = true) ?: false)
            val matchCategory = selectedCategory == "all" || product.category == selectedCategory
            matchSearch && matchCategory
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(4000)
            activeTestimonial = (activeTestimonial + 1) % testimonials.size
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0F172A),
                            Color(0xFF1E293B),
                            Color(0xFF0F172A)
                        )
                    )
                )
        )

        Box(
            modifier = Modifier
                .size(200.dp)
                .offset(x = 0.dp, y = 0.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color.Cyan.copy(alpha = 0.15f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
                .blur(80.dp)
                .zIndex(0f)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            item {
                HeroSection(
                    searchTerm = searchTerm,
                    onSearchTermChange = { searchTerm = it },
                    categories = categories,
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 32.dp)
                )
            }

            if (isLoading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color.Cyan)
                    }
                }
            }

            item {
                FeaturesSection(
                    features = features,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 64.dp)
                )
            }

            item {
                ProductsSection(
                    products = filteredProducts,
                    isTablet = isTablet,
                    onProductTap = { product ->
                        if (!isLoggedIn) {
                            onNavigate(AppRoute.Login)
                        } else {
                            onNavigate(AppRoute.ProductDetails(product.id))
                        }
                    },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 64.dp)
                )
            }

            item {
                WorkoutSection(
                    programs = workoutPrograms,
                    onProgramTap = { program ->
                        onNavigate(AppRoute.WorkoutProgram(program.id))
                    },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 64.dp)
                )
            }

            item {
                BlogSection(
                    posts = blogPosts,
                    onBlogTap = { },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 64.dp)
                )
            }

            item {
                TestimonialSection(
                    testimonial = testimonials[activeTestimonial],
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 64.dp)
                )
            }
        }
    }
}

@Composable
private fun HeroSection(
    searchTerm: String,
    onSearchTermChange: (String) -> Unit,
    categories: List<Category>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Color(0xFF0F172A).copy(alpha = 0.3f),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(16.dp)
            .padding(vertical = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "LEVEL UP\nYOUR",
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 64.sp
            )
            Text(
                text = "FITNESS",
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Cyan,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Premium supplements, elite equipment, and expert workout programs.",
                fontSize = 16.sp,
                color = Color(0xFFCBD5E1).copy(alpha = 0.9f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        SearchBar(
            searchTerm = searchTerm,
            onSearchTermChange = onSearchTermChange,
            placeholder = "Search products...",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories) { category ->
                CategoryButton(
                    id = category.id,
                    name = category.name,
                    icon = getCategoryIcon(category.icon),
                    isSelected = selectedCategory == category.id,
                    onClick = { onCategorySelected(category.id) }
                )
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
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

@Composable
private fun FeaturesSection(
    features: List<Feature>,
    modifier: Modifier = Modifier
) {
    val rows = features.chunked(2)
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        rows.forEach { rowItems ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                rowItems.forEach { feature ->
                    Box(modifier = Modifier.weight(1f)) {
                        FeatureCard(
                            icon = getFeatureIcon(feature.icon),
                            title = feature.title,
                            description = feature.description
                        )
                    }
                }
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun ProductsSection(
    products: List<com.supplementstore.models.Product>,
    isTablet: Boolean,
    onProductTap: (com.supplementstore.models.Product) -> Unit,
    modifier: Modifier = Modifier
) {
    val rows = products.chunked(if (isTablet) 2 else 1)
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("🔥", fontSize = 32.sp)
            Text(
                text = "Best Sellers",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Cyan
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            rows.forEach { rowItems ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    rowItems.forEach { product ->
                        Box(modifier = Modifier.weight(1f)) {
                            ProductCard(
                                product = ProductCardStoreProduct.from(product),
                                onTap = { onProductTap(product) }
                            )
                        }
                    }
                    if (rowItems.size == 1 && isTablet) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun WorkoutSection(
    programs: List<WorkoutProgram>,
    onProgramTap: (WorkoutProgram) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("🏋️", fontSize = 32.sp)
            Text(
                text = "Elite Workout Programs",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Cyan
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(programs) { program ->
                WorkoutCard(
                    image = program.image,
                    title = program.title,
                    description = program.description,
                    modifier = Modifier.clickable { onProgramTap(program) }
                )
            }
        }
    }
}

@Composable
private fun BlogSection(
    posts: List<BlogPost>,
    onBlogTap: (BlogPost) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("📝", fontSize = 32.sp)
            Text(
                text = "Latest Articles",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Cyan
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(posts) { post ->
                BlogCard(
                    image = post.image,
                    title = post.title,
                    date = post.date,
                    onTap = { onBlogTap(post) }
                )
            }
        }
    }
}

@Composable
private fun TestimonialSection(
    testimonial: Testimonial,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        TestimonialCard(testimonial = testimonial)
    }
}