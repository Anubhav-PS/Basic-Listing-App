package com.anubhav.swipetask.models

import com.squareup.moshi.Json

data class Product(
    @Json(name = "image")
    val image: String,
    @Json(name = "price")
    val price: Double,
    @Json(name = "product_name")
    val productName: String,
    @Json(name = "product_type")
    val productType: String,
    @Json(name = "tax")
    val tax: Double
)