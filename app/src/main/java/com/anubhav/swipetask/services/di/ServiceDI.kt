package com.anubhav.swipetask.services.di

import com.anubhav.swipetask.services.services.ProductsService
import com.anubhav.swipetask.utils.BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

const val baseUrl = BASE_URL

fun provideMoshi(): Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

fun provideRetrofit(baseUrl: String, moshi: Moshi) =
    Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

fun provideProductService(retrofit: Retrofit) = retrofit.create(ProductsService::class.java)
