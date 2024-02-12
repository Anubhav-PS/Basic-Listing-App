package com.anubhav.swipetask.services.models

import com.anubhav.swipetask.models.Product
import com.google.gson.annotations.SerializedName

data class ProductUploadResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("product_details")
    val productDetails: Product,
    @SerializedName("product_id")
    val productId: Int,
    @SerializedName("success")
    val success: Boolean
)