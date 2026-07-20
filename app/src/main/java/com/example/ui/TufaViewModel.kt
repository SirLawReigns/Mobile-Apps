package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.CartItem
import com.example.data.Product
import com.example.data.ProductColor
import com.example.data.TufaRepository
import com.example.data.WishlistItem
import com.example.data.User
import com.example.data.Order
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// Data structures for SHEIN-like elements
data class ProductReview(
    val id: String,
    val reviewerName: String,
    val rating: Int,
    val comment: String,
    val date: String
)

data class Coupon(
    val code: String,
    val discountPercent: Int,
    val description: String,
    val minSpend: Double = 0.0
)

class TufaViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TufaRepository

    // Current logged-in user
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    // Auth error handling
    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError.asStateFlow()

    // Pre-seeded Reviews State Flow
    private val _productReviews = MutableStateFlow<Map<String, List<ProductReview>>>(
        mapOf(
            "agbada_imperial" to listOf(
                ProductReview("1", "Gero L.", 5, "Remarkable styling and fabrics! Got non-stop compliments.", "Jul 18, 2026"),
                ProductReview("2", "Amina B.", 4, "Excellent tailoring and materials. Very luxury feel.", "Jul 15, 2026")
            ),
            "asooke_jacket" to listOf(
                ProductReview("3", "Chinedu O.", 5, "Absolutely gorgeous. The Aso-Oke weaving is incredibly rich.", "Jul 10, 2026"),
                ProductReview("4", "Sade T.", 4, "Excellent cropped design, goes beautifully with simple pants.", "Jul 11, 2026")
            ),
            "ankara_skirt" to listOf(
                ProductReview("5", "Kemi S.", 5, "Sizing is very accurate and the print is extremely vibrant!", "Jul 12, 2026")
            ),
            "kente_kaftan" to listOf(
                ProductReview("6", "Fatima Y.", 5, "Very breathable and elegant kaftan drape. 10/10!", "Jul 14, 2026")
            ),
            "senator_suit" to listOf(
                ProductReview("7", "Ibrahim A.", 5, "Classic fit. Elegant clean embroidery and precise sewing.", "Jul 16, 2026")
            ),
            "coral_cap" to listOf(
                ProductReview("8", "Chief Okey", 5, "Magnificent velvet crown with rich glass bead details.", "Jul 17, 2026")
            )
        )
    )
    val productReviews: StateFlow<Map<String, List<ProductReview>>> = _productReviews.asStateFlow()

    fun addReview(productId: String, reviewerName: String, rating: Int, comment: String) {
        val current = _productReviews.value.toMutableMap()
        val newList = current[productId].orEmpty().toMutableList()
        val dateFormat = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault())
        newList.add(
            ProductReview(
                id = java.util.UUID.randomUUID().toString(),
                reviewerName = reviewerName.ifBlank { "Verified Shopper" },
                rating = rating.coerceIn(1, 5),
                comment = comment.ifBlank { "Outstanding quality and modern styling!" },
                date = dateFormat.format(java.util.Date())
            )
        )
        current[productId] = newList
        _productReviews.value = current
    }

    // SHEIN Promo Coupons List
    val availableCoupons = listOf(
        Coupon("GERO15", 15, "15% OFF on all collections"),
        Coupon("GEROWELCOME", 10, "10% OFF Welcome Promo"),
        Coupon("GERO30", 30, "30% OFF on orders over ₦50,000", minSpend = 50000.0)
    )

    private val _selectedCoupon = MutableStateFlow<Coupon?>(null)
    val selectedCoupon: StateFlow<Coupon?> = _selectedCoupon.asStateFlow()

    fun applyCoupon(code: String): Boolean {
        val coupon = availableCoupons.find { it.code.trim().uppercase() == code.trim().uppercase() }
        return if (coupon != null) {
            _selectedCoupon.value = coupon
            true
        } else {
            false
        }
    }

    fun removeCoupon() {
        _selectedCoupon.value = null
    }

    // Advanced search & filter states
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _sortBy = MutableStateFlow("Default") // "Default", "PriceLowToHigh", "PriceHighToLow", "HighestRated"
    val sortBy: StateFlow<String> = _sortBy.asStateFlow()

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSortBy(sortOption: String) {
        _sortBy.value = sortOption
    }

    init {
        val database = AppDatabase.getDatabase(application)
        repository = TufaRepository(
            database.cartDao(),
            database.wishlistDao(),
            database.userDao(),
            database.orderDao()
        )

        // Seed users
        viewModelScope.launch {
            val adminUser = repository.getUserByEmail("admin@tufa.com")
            if (adminUser == null) {
                repository.insertUser(
                    User(
                        email = "admin@tufa.com",
                        passwordHash = "admin123",
                        fullName = "Jude (Founder)",
                        phone = "+234 801 234 5678",
                        address = "TUFA HQ, Lagos, Nigeria",
                        isAdmin = true
                    )
                )
            }

            val testUser = repository.getUserByEmail("jude@tufa.com")
            if (testUser == null) {
                repository.insertUser(
                    User(
                        email = "jude@tufa.com",
                        passwordHash = "password123",
                        fullName = "Jude Okafor",
                        phone = "+234 809 876 5432",
                        address = "12 Luxury Way, Ikoyi, Lagos",
                        isAdmin = false
                    )
                )
            }
        }
    }

    val cartItems: StateFlow<List<CartItem>> = repository.cartItems
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val wishlistItems: StateFlow<List<WishlistItem>> = repository.wishlistItems
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val allUsers: StateFlow<List<User>> = repository.allUsers
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val allOrders: StateFlow<List<Order>> = repository.allOrders
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Auth Operations
    fun clearAuthError() {
        _authError.value = null
    }

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _authError.value = null
            val user = repository.getUserByEmail(email.trim().lowercase())
            if (user == null) {
                _authError.value = "User not found"
            } else if (user.passwordHash != password) {
                _authError.value = "Incorrect password"
            } else {
                _currentUser.value = user
                onSuccess()
            }
        }
    }

    fun register(
        fullName: String,
        email: String,
        password: String,
        phone: String,
        address: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _authError.value = null
            val trimmedEmail = email.trim().lowercase()
            if (trimmedEmail.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
                _authError.value = "Please fill all required fields"
                return@launch
            }
            val existing = repository.getUserByEmail(trimmedEmail)
            if (existing != null) {
                _authError.value = "Email is already registered"
                return@launch
            }

            val newUser = User(
                email = trimmedEmail,
                passwordHash = password,
                fullName = fullName,
                phone = phone,
                address = address,
                isAdmin = false
            )
            val newId = repository.insertUser(newUser)
            _currentUser.value = newUser.copy(id = newId.toInt())
            onSuccess()
        }
    }

    fun logout() {
        _currentUser.value = null
        clearCart()
    }

    fun updateProfile(fullName: String, phone: String, address: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            val updated = user.copy(fullName = fullName, phone = phone, address = address)
            repository.updateUser(updated)
            _currentUser.value = updated
        }
    }

    // Place Order Operation
    fun placeOrder(addressOverride: String? = null, finalTotalOverride: Double? = null, onSuccess: () -> Unit) {
        val user = _currentUser.value ?: return
        val items = cartItems.value
        if (items.isEmpty()) return

        viewModelScope.launch {
            val summary = items.joinToString("\n") {
                "${it.productName} (${it.size}, ${it.color}) x${it.quantity}"
            }
            val subtotal = items.sumOf { it.price * it.quantity }
            val baseTotal = subtotal + 5000.00 // flat rate shipping ₦5,000
            val total = finalTotalOverride ?: baseTotal

            val order = Order(
                userId = user.id,
                orderDate = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault()).format(java.util.Date()),
                status = "Processing",
                totalAmount = total,
                itemsSummary = summary,
                shippingAddress = addressOverride ?: user.address.ifEmpty { "No address provided" }
            )
            repository.insertOrder(order)
            repository.clearCart()
            _selectedCoupon.value = null // Reset selected coupon on purchase
            onSuccess()
        }
    }

    fun updateOrderStatus(order: Order, newStatus: String) {
        viewModelScope.launch {
            repository.updateOrder(order.copy(status = newStatus))
        }
    }


    // Category Filter
    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    // Detail page states (size & color selections)
    private val _selectedSize = MutableStateFlow("M")
    val selectedSize: StateFlow<String> = _selectedSize.asStateFlow()

    private val _selectedColor = MutableStateFlow<ProductColor?>(null)
    val selectedColor: StateFlow<ProductColor?> = _selectedColor.asStateFlow()

    fun selectSize(size: String) {
        _selectedSize.value = size
    }

    fun selectColor(color: ProductColor) {
        _selectedColor.value = color
    }

    fun resetSelections(product: Product) {
        _selectedSize.value = product.sizes.firstOrNull() ?: "M"
        _selectedColor.value = product.colors.firstOrNull()
    }

    // Cart operations
    fun addToCart(product: Product, size: String, color: String, quantity: Int = 1) {
        viewModelScope.launch {
            // Check if item already exists in cart with same size and color
            val currentCart = cartItems.value
            val existingItem = currentCart.find {
                it.productId == product.id && it.size == size && it.color == color
            }

            if (existingItem != null) {
                repository.addCartItem(
                    existingItem.copy(quantity = existingItem.quantity + quantity)
                )
            } else {
                repository.addCartItem(
                    CartItem(
                        productId = product.id,
                        productName = product.name,
                        productCategory = product.category,
                        price = product.price,
                        size = size,
                        color = color,
                        quantity = quantity
                    )
                )
            }
        }
    }

    fun updateCartItemQuantity(item: CartItem, newQuantity: Int) {
        viewModelScope.launch {
            if (newQuantity <= 0) {
                repository.deleteCartItem(item.id)
            } else {
                repository.updateCartItem(item.copy(quantity = newQuantity))
            }
        }
    }

    fun removeCartItem(item: CartItem) {
        viewModelScope.launch {
            repository.deleteCartItem(item.id)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }

    // Wishlist operations
    fun toggleWishlist(product: Product) {
        viewModelScope.launch {
            val isCurrentlyWishlisted = wishlistItems.value.any { it.productId == product.id }
            if (isCurrentlyWishlisted) {
                repository.removeFromWishlist(product.id)
            } else {
                repository.addToWishlist(
                    WishlistItem(
                        productId = product.id,
                        productName = product.name,
                        productCategory = product.category,
                        price = product.price
                    )
                )
            }
        }
    }

    fun isProductWishlisted(productId: String): Boolean {
        return wishlistItems.value.any { it.productId == productId }
    }
}
