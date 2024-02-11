package com.anubhav.swipetask.utils.di

import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val utilsModule = module {
    single { provideConnectivityListener(androidApplication()) }
}
