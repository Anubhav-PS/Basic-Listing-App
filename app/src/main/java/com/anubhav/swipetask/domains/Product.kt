package com.anubhav.swipetask.domains

data class Product(
    val image: String,
    val price: Double,
    val productName: String,
    val productType: String,
    val tax: Double
)