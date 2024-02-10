package com.anubhav.swipetask.database.di

import android.content.Context
import androidx.room.Room
import com.anubhav.swipetask.database.room.AppDatabase
import com.anubhav.swipetask.utils.DATABASE_NAME

const val appDatabaseName = DATABASE_NAME

fun provideRoomDatabase(context: Context) =
    Room.databaseBuilder(context, AppDatabase::class.java, appDatabaseName).allowMainThreadQueries()
        .fallbackToDestructiveMigration().build()

fun provideProductsDao(database: AppDatabase) = database.productsDao()