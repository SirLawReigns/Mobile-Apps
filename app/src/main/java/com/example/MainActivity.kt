package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.CartItem
import com.example.data.Product
import com.example.data.ProductColor
import com.example.data.ProductData
import com.example.data.WishlistItem
import com.example.ui.TufaViewModel
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val tufaViewModel: TufaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                TufaApp(viewModel = tufaViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TufaApp(viewModel: TufaViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "explore"

    val cartItems by viewModel.cartItems.collectAsState()
    val wishlistItems by viewModel.wishlistItems.collectAsState()

    var showCheckoutSuccess by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            if (currentRoute == "explore" || currentRoute == "cart" || currentRoute == "wishlist" || currentRoute == "profile" || currentRoute == "admin" || currentRoute == "auth") {
                TopAppBar(
                    title = {
                        Text(
                            text = when (currentRoute) {
                                "auth" -> "SIGN IN"
                                "profile" -> "MY PROFILE"
                                "admin" -> "ADMIN CONSOLE"
                                else -> "TUFA CLOTHING BY JUDE"
                            },
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            letterSpacing = 1.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = if (currentRoute == "explore") TextAlign.Left else TextAlign.Center
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.primary
                    ),
                    actions = {
                        if (currentRoute == "explore") {
                            IconButton(
                                onClick = { navController.navigate("wishlist") },
                                modifier = Modifier.testTag("top_wishlist_button")
                            ) {
                                BadgedBox(
                                    badge = {
                                        if (wishlistItems.isNotEmpty()) {
                                            Badge { Text(wishlistItems.size.toString()) }
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FavoriteBorder,
                                        contentDescription = "Saved Items",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                            IconButton(
                                onClick = { navController.navigate("cart") },
                                modifier = Modifier.testTag("top_cart_button")
                            ) {
                                BadgedBox(
                                    badge = {
                                        if (cartItems.isNotEmpty()) {
                                            Badge { Text(cartItems.sumOf { it.quantity }.toString()) }
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ShoppingBag,
                                        contentDescription = "Shopping Cart",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (currentRoute == "explore" || currentRoute == "cart" || currentRoute == "wishlist" || currentRoute == "profile" || currentRoute == "admin" || currentRoute == "auth") {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp,
                    windowInsets = WindowInsets.navigationBars,
                    modifier = Modifier.testTag("bottom_nav")
                ) {
                    NavigationBarItem(
                        selected = currentRoute == "explore",
                        onClick = { navController.navigate("explore") },
                        icon = {
                            Icon(
                                imageVector = if (currentRoute == "explore") Icons.Default.ShoppingBag else Icons.Outlined.ShoppingBag,
                                contentDescription = "Shop"
                            )
                        },
                        label = { Text("Shop", fontFamily = FontFamily.SansSerif) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.secondary,
                            unselectedTextColor = MaterialTheme.colorScheme.secondary
                        ),
                        modifier = Modifier.testTag("nav_explore")
                    )
                    NavigationBarItem(
                        selected = currentRoute == "wishlist",
                        onClick = { navController.navigate("wishlist") },
                        icon = {
                            Icon(
                                imageVector = if (currentRoute == "wishlist") Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Saved"
                            )
                        },
                        label = { Text("Saved", fontFamily = FontFamily.SansSerif) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.secondary,
                            unselectedTextColor = MaterialTheme.colorScheme.secondary
                        ),
                        modifier = Modifier.testTag("nav_wishlist")
                    )
                    NavigationBarItem(
                        selected = currentRoute == "cart",
                        onClick = { navController.navigate("cart") },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.ShoppingBag,
                                contentDescription = "Bag"
                            )
                        },
                        label = {
                            BadgedBox(
                                badge = {
                                    if (cartItems.isNotEmpty()) {
                                        Badge { Text(cartItems.sumOf { it.quantity }.toString()) }
                                    }
                                }
                            ) {
                                Text("Bag", fontFamily = FontFamily.SansSerif)
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.secondary,
                            unselectedTextColor = MaterialTheme.colorScheme.secondary
                        ),
                        modifier = Modifier.testTag("nav_cart")
                    )
                    NavigationBarItem(
                        selected = currentRoute == "profile" || currentRoute == "admin" || currentRoute == "auth",
                        onClick = { navController.navigate("profile") },
                        icon = {
                            Icon(
                                imageVector = if (currentRoute == "profile" || currentRoute == "admin" || currentRoute == "auth") Icons.Default.Person else Icons.Outlined.Person,
                                contentDescription = "Profile"
                            )
                        },
                        label = { Text("Profile", fontFamily = FontFamily.SansSerif) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.secondary,
                            unselectedTextColor = MaterialTheme.colorScheme.secondary
                        ),
                        modifier = Modifier.testTag("nav_profile")
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "explore",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("explore") {
                ExploreScreen(
                    viewModel = viewModel,
                    onProductClick = { product ->
                        viewModel.resetSelections(product)
                        navController.navigate("detail/${product.id}")
                    }
                )
            }
            composable(
                route = "detail/{productId}",
                arguments = listOf(navArgument("productId") { type = NavType.StringType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId") ?: ""
                val product = ProductData.products.find { it.id == productId }
                if (product != null) {
                    DetailScreen(
                        product = product,
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
            composable("cart") {
                CartScreen(
                    viewModel = viewModel,
                    onCheckoutSuccess = { showCheckoutSuccess = true },
                    onExploreClick = { navController.navigate("explore") },
                    onAuthRedirect = { navController.navigate("profile") }
                )
            }
            composable("wishlist") {
                WishlistScreen(
                    viewModel = viewModel,
                    onProductClick = { product ->
                        viewModel.resetSelections(product)
                        navController.navigate("detail/${product.id}")
                    },
                    onExploreClick = { navController.navigate("explore") }
                )
            }
            composable("profile") {
                val currentUser by viewModel.currentUser.collectAsState()
                if (currentUser == null) {
                    AuthScreen(
                        viewModel = viewModel,
                        onAuthSuccess = {
                            navController.navigate("profile") {
                                popUpTo("profile") { inclusive = true }
                            }
                        }
                    )
                } else {
                    ProfileScreen(
                        viewModel = viewModel,
                        navController = navController
                    )
                }
            }
            composable("admin") {
                AdminDashboardScreen(
                    viewModel = viewModel,
                    navController = navController
                )
            }
        }

        // Animated Success Overlay
        AnimatedVisibility(
            visible = showCheckoutSuccess,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            CheckoutSuccessOverlay(
                onDismiss = {
                    showCheckoutSuccess = false
                    viewModel.clearCart()
                    navController.navigate("explore") {
                        popUpTo("explore") { inclusive = true }
                    }
                }
            )
        }
    }
}

@Composable
fun ExploreScreen(
    viewModel: TufaViewModel,
    onProductClick: (Product) -> Unit
) {
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val wishlistItems by viewModel.wishlistItems.collectAsState()

    val categories = listOf("All", "Outerwear", "Knitwear", "Pants", "Shirts", "Dresses")

    val filteredProducts = if (selectedCategory == "All") {
        ProductData.products
    } else {
        ProductData.products.filter { it.category == selectedCategory }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // Editorial Hero Banner (Using the generated 16:9 image)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                    .shadow(4.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_hero_banner),
                    contentDescription = "TUFA Clothing by Jude Brand Banner",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // Black Overlay gradient for text readability
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.65f)),
                                startY = 100f
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(24.dp)
                ) {
                    Text(
                        text = "THE ART OF REDUCTION",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 3.sp,
                        fontFamily = FontFamily.SansSerif
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Refined Essentials",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Light,
                        letterSpacing = 1.sp
                    )
                }
            }
        }

        // Category Selection Row
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "COLLECTIONS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { category ->
                    val isSelected = selectedCategory == category
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                            )
                            .clickable { viewModel.selectCategory(category) }
                            .padding(horizontal = 18.dp, vertical = 8.dp)
                            .testTag("category_chip_$category")
                    ) {
                        Text(
                            text = category,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                }
            }
        }

        // Product Count & Title
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (selectedCategory == "All") "CURATED PIECES" else selectedCategory.uppercase(),
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.5.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "${filteredProducts.size} Items",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    fontFamily = FontFamily.SansSerif
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Custom Grid Implementation inside LazyColumn
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val chunks = filteredProducts.chunked(2)
                chunks.forEach { rowProducts ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        rowProducts.forEach { product ->
                            Box(modifier = Modifier.weight(1f)) {
                                ProductCard(
                                    product = product,
                                    isWishlisted = wishlistItems.any { it.productId == product.id },
                                    onWishlistToggle = { viewModel.toggleWishlist(product) },
                                    onClick = { onProductClick(product) }
                                )
                            }
                        }
                        if (rowProducts.size < 2) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    isWishlisted: Boolean,
    onWishlistToggle: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("product_card_${product.id}"),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column {
            // Image Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(android.graphics.Color.parseColor(product.defaultHexAccent)),
                                MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    )
            ) {
                // Actual Product Image
                Image(
                    painter = painterResource(id = product.imageRes),
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Heart Icon Top Right
                IconButton(
                    onClick = onWishlistToggle,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(Color.White.copy(alpha = 0.8f), CircleShape)
                        .size(36.dp)
                        .testTag("wishlist_toggle_${product.id}")
                ) {
                    Icon(
                        imageVector = if (isWishlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Toggle Wishlist",
                        tint = if (isWishlisted) Color.Red else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Category tag bottom-left
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.85f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = product.category.uppercase(),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        fontFamily = FontFamily.SansSerif
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Details
            Text(
                text = product.name,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                fontFamily = FontFamily.Serif,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "₦${String.format("%,.2f", product.price)}",
                    fontSize = 14.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.secondary
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    product.colors.take(3).forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(Color(android.graphics.Color.parseColor(color.hexValue)))
                                .border(0.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), CircleShape)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DetailScreen(
    product: Product,
    viewModel: TufaViewModel,
    onBack: () -> Unit
) {
    val selectedSize by viewModel.selectedSize.collectAsState()
    val selectedColor by viewModel.selectedColor.collectAsState()
    val wishlistItems by viewModel.wishlistItems.collectAsState()

    val isWishlisted = wishlistItems.any { it.productId == product.id }
    val context = LocalContext.current

    LaunchedEffect(product) {
        viewModel.resetSelections(product)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            Surface(
                tonalElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Price Box
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "TOTAL PRICE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary,
                            letterSpacing = 1.sp,
                            fontFamily = FontFamily.SansSerif
                        )
                        Text(
                            text = "₦${String.format("%,.2f", product.price)}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Add to Bag Button
                    Button(
                        onClick = {
                            val colorName = selectedColor?.name ?: "Default"
                            viewModel.addToCart(product, selectedSize, colorName, 1)
                            Toast.makeText(context, "Added ${product.name} to bag", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .weight(2f)
                            .height(50.dp)
                            .testTag("add_to_bag_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ADD TO BAG",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Elegant Sticky Detail Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface, CircleShape)
                            .testTag("detail_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(
                        text = "PIECE DETAILS",
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 2.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    IconButton(
                        onClick = { viewModel.toggleWishlist(product) },
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface, CircleShape)
                            .testTag("detail_wishlist_toggle")
                    ) {
                        Icon(
                            imageVector = if (isWishlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Toggle Wishlist",
                            tint = if (isWishlisted) Color.Red else MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Big visual presentation card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(340.dp)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(android.graphics.Color.parseColor(product.defaultHexAccent)),
                                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                                )
                            )
                        )
                ) {
                    // Actual Product Image
                    Image(
                        painter = painterResource(id = product.imageRes),
                        contentDescription = product.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Collection accent label
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 24.dp)
                            .background(Color.White.copy(alpha = 0.85f), RoundedCornerShape(24.dp))
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "100% PURE MATERIALS",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                }
            }

            // Product Text Details
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = product.category.uppercase(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary,
                        letterSpacing = 2.sp,
                        fontFamily = FontFamily.SansSerif
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = product.name,
                        fontSize = 28.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = product.description,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                        fontFamily = FontFamily.SansSerif,
                        lineHeight = 22.sp
                    )
                }
            }

            // Selection Controls (Color swatch selection)
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "SELECT COLOR : ${selectedColor?.name ?: ""}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary,
                        letterSpacing = 1.5.sp,
                        fontFamily = FontFamily.SansSerif
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        product.colors.forEach { color ->
                            val isSelected = selectedColor?.name == color.name
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(android.graphics.Color.parseColor(color.hexValue)))
                                    .border(
                                        width = if (isSelected) 3.dp else 1.dp,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(
                                            alpha = 0.2f
                                        ),
                                        shape = CircleShape
                                    )
                                    .clickable { viewModel.selectColor(color) }
                                    .testTag("color_swatch_${color.name}")
                            )
                        }
                    }
                }
            }

            // Size Selector Row
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "SELECT SIZE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary,
                        letterSpacing = 1.5.sp,
                        fontFamily = FontFamily.SansSerif
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        product.sizes.forEach { size ->
                            val isSelected = selectedSize == size
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.surface
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = if (isSelected) Color.Transparent else MaterialTheme.colorScheme.primary.copy(
                                            alpha = 0.2f
                                        ),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable { viewModel.selectSize(size) }
                                    .testTag("size_chip_$size"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = size,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.SansSerif
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CartScreen(
    viewModel: TufaViewModel,
    onCheckoutSuccess: () -> Unit,
    onExploreClick: () -> Unit,
    onAuthRedirect: () -> Unit
) {
    val cartItems by viewModel.cartItems.collectAsState()

    if (cartItems.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.ShoppingBag,
                contentDescription = "Empty Bag",
                modifier = Modifier
                    .size(80.dp)
                    .padding(bottom = 16.dp),
                tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
            )
            Text(
                text = "YOUR BAG IS EMPTY",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                letterSpacing = 1.5.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Discover items designed for contemporary life.",
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.SansSerif
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onExploreClick,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("discover_pieces_button")
            ) {
                Text(
                    text = "EXPLORE COLLECTION",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    letterSpacing = 1.sp
                )
            }
        }
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "SHOPPING BAG (${cartItems.sumOf { it.quantity }})",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        letterSpacing = 1.sp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                items(cartItems, key = { it.id }) { item ->
                    val product = ProductData.products.find { it.id == item.productId }
                    val itemAccent = product?.defaultHexAccent ?: "#D4C5B3"

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(2.dp, RoundedCornerShape(16.dp))
                            .testTag("cart_item_${item.id}"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Mini thumbnail
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(android.graphics.Color.parseColor(itemAccent))),
                                contentAlignment = Alignment.Center
                            ) {
                                if (product != null) {
                                    Image(
                                        painter = painterResource(id = product.imageRes),
                                        contentDescription = item.productName,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Text(
                                        text = item.productName.first().toString(),
                                        fontFamily = FontFamily.Serif,
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Light,
                                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            // Details
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.productName,
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 15.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Size: ${item.size}  |  Color: ${item.color}",
                                    fontFamily = FontFamily.SansSerif,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "₦${String.format("%,.2f", item.price)}",
                                    fontFamily = FontFamily.SansSerif,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            // Quantity adjustments
                            Column(
                                horizontalAlignment = Alignment.End,
                                verticalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxHeight()
                            ) {
                                IconButton(
                                    onClick = { viewModel.removeCartItem(item) },
                                    modifier = Modifier.testTag("remove_item_${item.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Remove item",
                                        tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    IconButton(
                                        onClick = { viewModel.updateCartItemQuantity(item, item.quantity - 1) },
                                        colors = IconButtonDefaults.iconButtonColors(
                                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                                        ),
                                        modifier = Modifier
                                            .size(28.dp)
                                            .testTag("qty_minus_${item.id}")
                                    ) {
                                        Icon(Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(14.dp))
                                    }

                                    Text(
                                        text = item.quantity.toString(),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        fontFamily = FontFamily.SansSerif
                                    )

                                    IconButton(
                                        onClick = { viewModel.updateCartItemQuantity(item, item.quantity + 1) },
                                        colors = IconButtonDefaults.iconButtonColors(
                                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                                        ),
                                        modifier = Modifier
                                            .size(28.dp)
                                            .testTag("qty_plus_${item.id}")
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = "Increase", modifier = Modifier.size(14.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Summary Card at bottom
            Surface(
                tonalElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    val subtotal = cartItems.sumOf { it.price * it.quantity }
                    val shipping = 0.00 // Free brand shipping
                    val total = subtotal + shipping

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "SUBTOTAL",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.secondary,
                            fontFamily = FontFamily.SansSerif,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "₦${String.format("%,.2f", subtotal)}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "SHIPPING",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.secondary,
                            fontFamily = FontFamily.SansSerif,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "FREE",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF43A047),
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "GRAND TOTAL",
                            fontSize = 14.sp,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "₦${String.format("%,.2f", total)}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    val currentUser by viewModel.currentUser.collectAsState()

                    Button(
                        onClick = {
                            if (currentUser == null) {
                                onAuthRedirect()
                            } else {
                                viewModel.placeOrder {
                                    onCheckoutSuccess()
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("checkout_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (currentUser == null) "SIGN IN TO CHECK OUT" else "PLACE ORDER & DISPATCH NOW",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WishlistScreen(
    viewModel: TufaViewModel,
    onProductClick: (Product) -> Unit,
    onExploreClick: () -> Unit
) {
    val wishlistItems by viewModel.wishlistItems.collectAsState()

    if (wishlistItems.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = "Empty Wishlist",
                modifier = Modifier
                    .size(80.dp)
                    .padding(bottom = 16.dp),
                tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
            )
            Text(
                text = "NO SAVED ITEMS",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                letterSpacing = 1.5.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Save items that inspire you to build your signature look.",
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.SansSerif
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onExploreClick,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("wishlist_explore_button")
            ) {
                Text(
                    text = "DISCOVER PIECES",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    letterSpacing = 1.sp
                )
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "SAVED ITEMS (${wishlistItems.size})",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                letterSpacing = 1.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(wishlistItems) { wishlistItem ->
                    val product = ProductData.products.find { it.id == wishlistItem.productId }
                    if (product != null) {
                        ProductCard(
                            product = product,
                            isWishlisted = true,
                            onWishlistToggle = { viewModel.toggleWishlist(product) },
                            onClick = { onProductClick(product) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CheckoutSuccessOverlay(onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.85f))
            .clickable(enabled = false) {}, // absorb taps
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .shadow(12.dp, RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(24.dp))
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Success",
                modifier = Modifier.size(72.dp),
                tint = Color(0xFF43A047)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "ORDER CONFIRMED",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                letterSpacing = 1.5.sp,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Thank you for your purchase. Your TUFA Clothing by Jude essentials are being prepared for dispatch.",
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.SansSerif,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("dismiss_success_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "CONTINUE SHOPPING",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    fontFamily = FontFamily.SansSerif
                )
            }
        }
    }
}

@Composable
fun AuthScreen(
    viewModel: TufaViewModel,
    onAuthSuccess: () -> Unit
) {
    val context = LocalContext.current
    var isSignUp by remember { mutableStateOf(false) }
    
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    
    var passwordVisible by remember { mutableStateOf(false) }
    val authError by viewModel.authError.collectAsState()
    
    LaunchedEffect(authError) {
        if (authError != null) {
            Toast.makeText(context, authError, Toast.LENGTH_LONG).show()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        item {
            Spacer(modifier = Modifier.height(32.dp))
            
            // Brand Logo Visual
            Text(
                text = "TUFA",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Light,
                fontSize = 48.sp,
                letterSpacing = 8.sp,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = "CONTEMPORARY AFRICAN COUTURE",
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Medium,
                fontSize = 10.sp,
                letterSpacing = 2.sp,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp, bottom = 32.dp)
            )

            Text(
                text = if (isSignUp) "CREATE ACCOUNT" else "WELCOME BACK",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                letterSpacing = 1.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = if (isSignUp) "Register to save custom styles & track deliveries" else "Sign in to complete purchases and view your wardrobe",
                fontFamily = FontFamily.SansSerif,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 24.dp)
            )

            // Input Fields
            if (isSignUp) {
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("FULL NAME *") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp).testTag("auth_name_input"),
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )
            }

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("EMAIL ADDRESS *") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp).testTag("auth_email_input"),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("PASSWORD *") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp).testTag("auth_password_input"),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )

            if (isSignUp) {
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("PHONE NUMBER") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp).testTag("auth_phone_input"),
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("SHIPPING ADDRESS") },
                    leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp).testTag("auth_address_input"),
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )
            }

            // Error Message Display
            if (authError != null) {
                Text(
                    text = authError ?: "",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // Submit Button
            Button(
                onClick = {
                    if (isSignUp) {
                        viewModel.register(fullName, email, password, phone, address, onAuthSuccess)
                    } else {
                        viewModel.login(email, password, onAuthSuccess)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("auth_submit_button"),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = if (isSignUp) "CREATE ACCOUNT" else "SIGN IN",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Toggle Text
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isSignUp) "Already have an account?" else "Don't have an account?",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isSignUp) "Sign In" else "Sign Up",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable {
                            isSignUp = !isSignUp
                            viewModel.clearAuthError()
                        }
                        .testTag("auth_toggle_button")
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Testing Shortcut Section for Reviewers
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "QUICK FILL / TEST ACCOUNTS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                email = "jude@tufa.com"
                                password = "password123"
                                isSignUp = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f), contentColor = MaterialTheme.colorScheme.primary),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.weight(1f).height(36.dp).testTag("quick_user_btn")
                        ) {
                            Text("Standard User", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = {
                                email = "admin@tufa.com"
                                password = "admin123"
                                isSignUp = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f), contentColor = MaterialTheme.colorScheme.primary),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.weight(1f).height(36.dp).testTag("quick_admin_btn")
                        ) {
                            Text("Admin Console", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(
    viewModel: TufaViewModel,
    navController: NavController
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val allOrders by viewModel.allOrders.collectAsState()
    
    val userOrders = remember(allOrders, currentUser) {
        allOrders.filter { it.userId == currentUser?.id }
    }
    
    var isEditing by remember { mutableStateOf(false) }
    var fullName by remember { mutableStateOf(currentUser?.fullName ?: "") }
    var phone by remember { mutableStateOf(currentUser?.phone ?: "") }
    var address by remember { mutableStateOf(currentUser?.address ?: "") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Profile Header / Avatar Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Initials Avatar
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (currentUser?.fullName?.firstOrNull() ?: 'U').uppercase().toString(),
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column {
                        Text(
                            text = currentUser?.fullName ?: "Anonymous User",
                            fontFamily = FontFamily.Serif,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = currentUser?.email ?: "",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        if (currentUser?.isAdmin == true) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AdminPanelSettings,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "FOUNDER / ADMIN",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Admin Access Section
        if (currentUser?.isAdmin == true) {
            item {
                Button(
                    onClick = { navController.navigate("admin") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("admin_panel_button"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.AdminPanelSettings, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ACCESS ADMIN CONSOLE",
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Personal Information
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SHIPPING DETAILS",
                            fontFamily = FontFamily.Serif,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = if (isEditing) "SAVE" else "EDIT",
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .clickable {
                                    if (isEditing) {
                                        viewModel.updateProfile(fullName, phone, address)
                                    }
                                    isEditing = !isEditing
                                }
                                .testTag("edit_profile_toggle")
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    if (isEditing) {
                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            label = { Text("FULL NAME") },
                            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp).testTag("profile_name_edit"),
                            shape = RoundedCornerShape(8.dp)
                        )
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("PHONE") },
                            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp).testTag("profile_phone_edit"),
                            shape = RoundedCornerShape(8.dp)
                        )
                        OutlinedTextField(
                            value = address,
                            onValueChange = { address = it },
                            label = { Text("SHIPPING ADDRESS") },
                            modifier = Modifier.fillMaxWidth().testTag("profile_address_edit"),
                            shape = RoundedCornerShape(8.dp)
                        )
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(verticalAlignment = Alignment.Top) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = currentUser?.fullName ?: "Not provided", fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
                            }
                            Row(verticalAlignment = Alignment.Top) {
                                Icon(Icons.Default.Phone, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = currentUser?.phone?.ifEmpty { "Not provided" } ?: "Not provided", fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
                            }
                            Row(verticalAlignment = Alignment.Top) {
                                Icon(Icons.Default.Home, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = currentUser?.address?.ifEmpty { "Not provided" } ?: "Not provided", fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
        }

        // Active Perks & Promotions
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "BRAND PERKS & OFFERS",
                        fontFamily = FontFamily.Serif,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("10% OFF Welcome Promo", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text("Valid on your first purchase", fontSize = 11.sp, color = MaterialTheme.colorScheme.secondary)
                        }
                        Box(
                            modifier = Modifier
                                .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("TUFAWELCOME", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }

        // Order History
        item {
            Text(
                text = "ORDER HISTORY (${userOrders.size})",
                fontFamily = FontFamily.Serif,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        if (userOrders.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Outlined.ShoppingBag, contentDescription = null, tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f), modifier = Modifier.size(36.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("No order history found", fontSize = 13.sp, color = MaterialTheme.colorScheme.secondary)
                        }
                    }
                }
            }
        } else {
            items(userOrders) { order ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "ORDER ID: #${order.id}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            
                            // Colored status tag
                            val statusColor = when (order.status) {
                                "Delivered" -> Color(0xFF43A047)
                                "Shipped" -> Color(0xFF00ACC1)
                                "Processing" -> Color(0xFF1E88E5)
                                else -> Color(0xFFF4511E)
                            }
                            
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(statusColor.copy(alpha = 0.1f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = order.status.uppercase(),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = statusColor
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Date: ${order.orderDate}", fontSize = 11.sp, color = MaterialTheme.colorScheme.secondary)
                        
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .height(1.dp)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f))
                        )
                        
                        Text(
                            text = order.itemsSummary,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.primary,
                            lineHeight = 18.sp
                        )
                        
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .height(1.dp)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f))
                        )
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Paid (Cash on Delivery)", fontSize = 11.sp, color = MaterialTheme.colorScheme.secondary)
                            Text(
                                text = "₦${String.format("%,.2f", order.totalAmount)}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }

        // Logout Button
        item {
            Button(
                onClick = { viewModel.logout() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 32.dp)
                    .height(48.dp)
                    .testTag("logout_button"),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "LOG OUT OF ACCOUNT",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun AdminDashboardScreen(
    viewModel: TufaViewModel,
    navController: NavController
) {
    val allOrders by viewModel.allOrders.collectAsState()
    val allUsers by viewModel.allUsers.collectAsState()
    
    var activeTab by remember { mutableStateOf("orders") } // "orders" or "users"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Simple Back Bar / Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.testTag("admin_back_button")
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "ADMIN CONTROL DASHBOARD",
                fontFamily = FontFamily.Serif,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 0.5.sp
            )
        }

        // Custom segmented control tab bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f))
        ) {
            Button(
                onClick = { activeTab = "orders" },
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .testTag("admin_orders_tab"),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (activeTab == "orders") MaterialTheme.colorScheme.primary else Color.Transparent,
                    contentColor = if (activeTab == "orders") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
                )
            ) {
                Text("ORDERS (${allOrders.size})", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = { activeTab = "users" },
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .testTag("admin_users_tab"),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (activeTab == "users") MaterialTheme.colorScheme.primary else Color.Transparent,
                    contentColor = if (activeTab == "users") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
                )
            ) {
                Text("CUSTOMERS (${allUsers.size})", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        if (activeTab == "orders") {
            if (allOrders.isEmpty()) {
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No customer orders found", color = MaterialTheme.colorScheme.secondary)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(allOrders) { order ->
                        val customer = allUsers.find { it.id == order.userId }
                        
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(text = "ORDER ID: #${order.id}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text(text = "Customer: ${customer?.fullName ?: "Anonymous"}", fontSize = 11.sp, color = MaterialTheme.colorScheme.secondary)
                                    }
                                    
                                    // Order Status drop-down picker trigger
                                    var showDropdown by remember { mutableStateOf(false) }
                                    Box {
                                        val statusColor = when (order.status) {
                                            "Delivered" -> Color(0xFF43A047)
                                            "Shipped" -> Color(0xFF00ACC1)
                                            "Processing" -> Color(0xFF1E88E5)
                                            else -> Color(0xFFF4511E)
                                        }
                                        
                                        Row(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(statusColor.copy(alpha = 0.1f))
                                                .clickable { showDropdown = true }
                                                .padding(horizontal = 8.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = order.status.uppercase(),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = statusColor
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Icon(
                                                imageVector = Icons.Default.Add,
                                                contentDescription = "Change Status",
                                                tint = statusColor,
                                                modifier = Modifier.size(10.dp)
                                            )
                                        }
                                        
                                        DropdownMenu(
                                            expanded = showDropdown,
                                            onDismissRequest = { showDropdown = false }
                                        ) {
                                            listOf("Pending", "Processing", "Shipped", "Delivered").forEach { statusName ->
                                                DropdownMenuItem(
                                                    text = { Text(statusName) },
                                                    onClick = {
                                                        viewModel.updateOrderStatus(order, statusName)
                                                        showDropdown = false
                                                    },
                                                    modifier = Modifier.testTag("status_item_${statusName.lowercase()}")
                                                )
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Items Ordered:\n${order.itemsSummary}",
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Dispatch Location:\n${order.shippingAddress}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.secondary,
                                    lineHeight = 16.sp
                                )
                                
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp)
                                        .height(1.dp)
                                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f))
                                )
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "Date: ${order.orderDate}", fontSize = 11.sp, color = MaterialTheme.colorScheme.secondary)
                                    Text(
                                        text = "₦${String.format("%,.2f", order.totalAmount)}",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } else {
            if (allUsers.isEmpty()) {
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No customer accounts found", color = MaterialTheme.colorScheme.secondary)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(allUsers) { user ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = user.fullName,
                                        fontFamily = FontFamily.Serif,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    if (user.isAdmin) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "ADMIN",
                                                fontSize = 8.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "Email: ${user.email}", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                                Text(text = "Phone: ${user.phone.ifEmpty { "Not set" }}", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                                Text(text = "Address: ${user.address.ifEmpty { "Not set" }}", fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
                            }
                        }
                    }
                }
            }
        }
    }
}
