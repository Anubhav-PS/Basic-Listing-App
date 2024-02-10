package com.anubhav.swipetask.repositories

import android.util.Log
import com.anubhav.swipetask.models.Product
import com.anubhav.swipetask.repositories.models.DataStatus
import com.anubhav.swipetask.services.services.ProductsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ProductsRepository(private val productService: ProductsService) {

    private val TAG = "Products-Repository"
    suspend fun getProduct(query: String) {

    }

    suspend fun getAllProduct() = flow {
        emit(DataStatus.loading())
        val networkResult = productService.getProducts()
        when (networkResult.code()) {
            200 -> {
                val productList = storeProductNUpdateLastSync(networkResult.body())
                emit(DataStatus.success(productList))
            }

            400 -> emit(DataStatus.failed(networkResult.message()))
            500 -> emit(DataStatus.failed(networkResult.message()))
        }
    }.catch { emit(DataStatus.failed(it.message.toString())) }
        .flowOn(Dispatchers.IO)

    suspend fun postProduct() {

    }

    private suspend fun storeProductNUpdateLastSync(productList: List<Product>?): List<Product> {
        //store to db
        //update the last sync
        //return the db
        return listOf()
    }

}