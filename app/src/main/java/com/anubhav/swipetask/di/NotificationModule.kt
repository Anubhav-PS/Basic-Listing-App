package com.anubhav.swipetask.di

import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val notificationModule = module {
    single { provideNotificationBuilder(androidApplication()) }
    single { provideNotificationManager(androidApplication()) }
}