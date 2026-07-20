package com.example.data

import kotlinx.coroutines.flow.Flow

class TufaRepository(
    private val cartDao: CartDao,
    private val wishlistDao: WishlistDao,
    private val userDao: UserDao,
    private val orderDao: OrderDao
) {
    val cartItems: Flow<List<CartItem>> = cartDao.getCartItems()
    val wishlistItems: Flow<List<WishlistItem>> = wishlistDao.getWishlistItems()
    val allUsers: Flow<List<User>> = userDao.getAllUsers()
    val allOrders: Flow<List<Order>> = orderDao.getAllOrders()

    suspend fun addCartItem(item: CartItem) {
        cartDao.insertCartItem(item)
    }

    suspend fun updateCartItem(item: CartItem) {
        cartDao.updateCartItem(item)
    }

    suspend fun deleteCartItem(id: Int) {
        cartDao.deleteCartItem(id)
    }

    suspend fun clearCart() {
        cartDao.clearCart()
    }

    suspend fun addToWishlist(item: WishlistItem) {
        wishlistDao.addToWishlist(item)
    }

    suspend fun removeFromWishlist(productId: String) {
        wishlistDao.removeFromWishlist(productId)
    }

    fun isWishlisted(productId: String): Flow<Boolean> {
        return wishlistDao.isWishlisted(productId)
    }

    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }

    suspend fun insertUser(user: User): Long {
        return userDao.insertUser(user)
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    fun getOrdersForUser(userId: Int): Flow<List<Order>> {
        return orderDao.getOrdersForUser(userId)
    }

    suspend fun insertOrder(order: Order): Long {
        return orderDao.insertOrder(order)
    }

    suspend fun updateOrder(order: Order) {
        orderDao.updateOrder(order)
    }
}
