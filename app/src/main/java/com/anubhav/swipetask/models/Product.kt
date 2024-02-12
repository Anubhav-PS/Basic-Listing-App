package com.anubhav.swipetask.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anubhav.swipetask.utils.PRODUCTS_TABLE_NAME
import com.google.gson.annotations.SerializedName

@Entity(tableName = PRODUCTS_TABLE_NAME)
data class Product(
    @SerializedName("image")
    val image: String,
    @SerializedName("price")
    val price: Double,
    @PrimaryKey(autoGenerate = false)
    @SerializedName("product_name")
    val productName: String,
    @SerializedName("product_type")
    val productType: String,
    @SerializedName("tax")
    val tax: Double,
)