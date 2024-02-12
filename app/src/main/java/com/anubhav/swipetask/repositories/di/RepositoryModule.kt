package com.anubhav.swipetask.repositories.di

import com.anubhav.swipetask.repositories.ProductsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    factory { ProductsRepository(get(), get(), androidContext()) }
}