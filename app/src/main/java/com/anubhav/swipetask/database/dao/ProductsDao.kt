package com.anubhav.swipetask.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anubhav.swipetask.models.Product
import com.anubhav.swipetask.utils.PRODUCTS_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: List<Product>): List<Long>

    @Query("DELETE FROM $PRODUCTS_TABLE_NAME")
    suspend fun delete()

    @Query("SELECT * FROM $PRODUCTS_TABLE_NAME")
    fun getAllProducts(): Flow<MutableList<Product>>

    @Query("SELECT * FROM $PRODUCTS_TABLE_NAME WHERE productName LIKE '%' || :query || '%'")
    fun searchForProductName(query: String): Flow<List<Product>>

}