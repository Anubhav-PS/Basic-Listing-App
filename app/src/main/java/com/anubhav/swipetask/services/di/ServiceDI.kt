package com.anubhav.swipetask.services.di

import com.anubhav.swipetask.services.services.ProductsService
import com.anubhav.swipetask.utils.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val baseUrl = BASE_URL

fun provideRetrofit(baseUrl: String) =
    Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .build()

fun provideProductService(retrofit: Retrofit) = retrofit.create(ProductsService::class.java)
