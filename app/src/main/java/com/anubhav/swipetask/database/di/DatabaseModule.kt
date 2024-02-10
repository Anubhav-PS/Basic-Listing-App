package com.anubhav.swipetask.database.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single { appDatabaseName }
    single { provideRoomDatabase(androidContext()) }
    single { provideProductsDao(get()) }
}