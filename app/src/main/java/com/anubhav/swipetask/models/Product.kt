package com.anubhav.swipetask.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anubhav.swipetask.utils.PRODUCTS_TABLE_NAME
import com.squareup.moshi.Json

@Entity(tableName = PRODUCTS_TABLE_NAME)
data class Product(
    @Json(name = "image")
    val image: String,
    @Json(name = "price")
    val price: Double,
    @PrimaryKey(autoGenerate = false)
    @Json(name = "product_name")
    val productName: String,
    @Json(name = "product_type")
    val productType: String,
    @Json(name = "tax")
    val tax: Double,
)