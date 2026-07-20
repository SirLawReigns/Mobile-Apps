package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: String,
    val productName: String,
    val productCategory: String,
    val price: Double,
    val size: String,
    val color: String,
    val quantity: Int
)

@Entity(tableName = "wishlist_items")
data class WishlistItem(
    @PrimaryKey val productId: String,
    val productName: String,
    val productCategory: String,
    val price: Double
)

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val email: String,
    val passwordHash: String,
    val fullName: String,
    val phone: String,
    val address: String,
    val isAdmin: Boolean = false
)

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val orderDate: String,
    val status: String, // e.g., "Pending", "Processing", "Shipped", "Delivered"
    val totalAmount: Double,
    val itemsSummary: String,
    val shippingAddress: String
)

