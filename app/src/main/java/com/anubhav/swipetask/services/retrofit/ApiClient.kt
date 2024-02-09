package com.anubhav.swipetask.services.retrofit

import android.util.Log
import com.anubhav.swipetask.domains.Product
import com.anubhav.swipetask.services.services.ProductsService
import retrofit2.Response

class ApiClient(
    private val productsService: ProductsService
) {

    suspend fun getAllProducts(): ServerResponse<List<Product>> {
        return apiCall { productsService.getProducts() }
    }

    suspend fun postProduct() {

    }

    private inline fun <T> apiCall(call: () -> Response<T>): ServerResponse<T> {
        return try {
            ServerResponse.success(call.invoke())
        } catch (e: Exception) {
            Log.e("Server Error", "${e.message}")
            ServerResponse.failure(e)
        }
    }

}
