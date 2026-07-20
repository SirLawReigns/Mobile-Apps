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

class TufaViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TufaRepository

    // Current logged-in user
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    // Auth error handling
    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError.asStateFlow()

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
    fun placeOrder(addressOverride: String? = null, onSuccess: () -> Unit) {
        val user = _currentUser.value ?: return
        val items = cartItems.value
        if (items.isEmpty()) return

        viewModelScope.launch {
            val summary = items.joinToString("\n") {
                "${it.productName} (${it.size}, ${it.color}) x${it.quantity}"
            }
            val subtotal = items.sumOf { it.price * it.quantity }
            val total = subtotal + 5000.00 // flat rate shipping ₦5,000

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
