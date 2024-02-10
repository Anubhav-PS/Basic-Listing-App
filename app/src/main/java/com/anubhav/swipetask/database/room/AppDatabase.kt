package com.anubhav.swipetask.database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.anubhav.swipetask.database.dao.ProductsDao
import com.anubhav.swipetask.models.Product

@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productsDao(): ProductsDao

}