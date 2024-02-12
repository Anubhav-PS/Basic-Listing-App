package com.anubhav.swipetask.services.di

import org.koin.dsl.module

val serviceModule = module {
    single { baseUrl }
    single { provideRetrofit(get()) }
    single { provideProductService(get()) }
}
