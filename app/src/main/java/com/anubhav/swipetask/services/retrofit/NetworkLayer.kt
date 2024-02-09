package com.anubhav.swipetask.services.retrofit

import com.anubhav.swipetask.services.services.ProductsService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class NetworkLayer {

    companion object {

        private val TAG = "Network-Layer"
        private val BASE_URL = "https://app.getswipe.in/"
        private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        private var productsService = retrofit.create(ProductsService::class.java)

        fun getApiClient(): ApiClient {
            return ApiClient(productsService)
        }

    }
}