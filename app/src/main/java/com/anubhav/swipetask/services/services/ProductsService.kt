package com.anubhav.swipetask.services.services

import com.anubhav.swipetask.models.Product
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface ProductsService {

    @GET("api/public/get")
    suspend fun getProducts(): Response<List<Product>>

    @POST("api/public/add")
    suspend fun postProduct()

}