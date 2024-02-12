package com.anubhav.swipetask.services.services

import com.anubhav.swipetask.models.Product
import com.anubhav.swipetask.services.models.ProductUploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ProductsService {

    @GET("api/public/get")
    suspend fun getProducts(): Response<MutableList<Product>>

    @Multipart
    @POST("api/public/add")
    fun postProductWithImage(
        @Part("product_name") productName: RequestBody,
        @Part("product_type") productType: RequestBody,
        @Part("price") productPrice: RequestBody,
        @Part("tax") productTax: RequestBody,
        @Part productImage: MultipartBody.Part,
    ): Call<ProductUploadResponse>

    @Multipart
    @POST("api/public/add")
    fun postProductWithoutImage(
        @Part("product_name") productName: RequestBody,
        @Part("product_type") productType: RequestBody,
        @Part("price") productPrice: RequestBody,
        @Part("tax") productTax: RequestBody
    ): Call<ProductUploadResponse>

}