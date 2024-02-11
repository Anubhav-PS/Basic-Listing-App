package com.anubhav.swipetask.repositories

import android.util.Log
import com.anubhav.swipetask.database.dao.ProductsDao
import com.anubhav.swipetask.models.Product
import com.anubhav.swipetask.repositories.models.DataStatus
import com.anubhav.swipetask.services.services.ProductsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.toList

class ProductsRepository(
    private val productService: ProductsService,
    private val productsDao: ProductsDao,
) {

    private val TAG = "Products-Repository"
    val allProducts: Flow<MutableList<Product>> = productsDao.getAllProducts()

    suspend fun pullProductsFromServer() = flow {
        emit(DataStatus.loading())
        val networkResult = productService.getProducts()
        when (networkResult.code()) {
            200 -> {
                val productListFromNetwork = networkResult.body() ?: emptyList()
                if (productListFromNetwork.isNotEmpty()) {
                    emit(DataStatus.success(productListFromNetwork))
                }
            }

            400 -> emit(DataStatus.failed(networkResult.message().toString()))
            500 -> emit(DataStatus.failed(networkResult.message().toString()))
        }
    }.catch { emit(DataStatus.failed(it.message.toString())) }
        .flowOn(Dispatchers.IO)

    suspend fun storeProduct(product: Product) = productsDao.insertProduct(product)

    suspend fun storeProduct(products: List<Product>) {
        productsDao.insertProduct(products)
    }

    suspend fun searchProduct(query: String) = flow {
        emit(DataStatus.loading())
        productsDao.searchForProductName(query).collect {
            emit(DataStatus.success(it))
        }
    }.catch { emit(DataStatus.failed(it.message.toString())) }.flowOn(Dispatchers.IO)

}