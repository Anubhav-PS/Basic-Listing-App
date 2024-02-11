package com.anubhav.swipetask.services.di

import org.koin.dsl.module

val serviceModule = module {
    single { baseUrl }
    single { provideMoshi() }
    single { provideRetrofit(get(), get()) }
    single { provideProductService(get()) }
}
