package com.anubhav.swipetask.services.di

import com.anubhav.swipetask.services.services.ProductsService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

const val baseUrl = "https://app.getswipe.in/"
fun provideMoshi(): Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
fun provideRetrofit(baseUrl: String, moshi: Moshi) =
    Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
fun provideProductService(retrofit: Retrofit) = retrofit.create(ProductsService::class.java)

val serviceModule = module {
    single { baseUrl }
    single { provideMoshi() }
    single { provideRetrofit(get(),get()) }
    single { provideProductService(get()) }
}