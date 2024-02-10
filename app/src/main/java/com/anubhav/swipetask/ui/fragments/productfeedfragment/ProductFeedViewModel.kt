package com.anubhav.swipetask.ui.fragments.productfeedfragment

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.anubhav.swipetask.models.Product
import com.anubhav.swipetask.repositories.ProductsRepository
import com.anubhav.swipetask.repositories.models.DataStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductFeedViewModel(private val productRepo: ProductsRepository) : ViewModel(),
    LifecycleEventObserver {

    private val TAG = "Product-Feed-View-Model"
    val productList: LiveData<List<Product>> = productRepo.allProducts.asLiveData()
    private val _productListFromNetworkStatus: MutableLiveData<DataStatus.Status> = MutableLiveData()
    var productListFromNetworkStatus: LiveData<DataStatus.Status> = _productListFromNetworkStatus

    private fun pullProductsFromServer() = viewModelScope.launch(Dispatchers.IO) {
        _productListFromNetworkStatus.postValue(DataStatus.Status.Loading)
        productRepo.pullProductsFromServer().collect {
            when (it.status) {
                DataStatus.Status.Failed -> {
                    _productListFromNetworkStatus.postValue(DataStatus.Status.Failed)
                }

                DataStatus.Status.Loading -> {
                    _productListFromNetworkStatus.postValue(DataStatus.Status.Loading)
                }

                DataStatus.Status.Success -> {
                    it.data?.apply {
                        Log.i(TAG,"List is $this")
                        saveProductsToDB(this)
                    }
                }
            }
        }
    }

    private fun saveProductsToDB(productList: List<Product>) =
        viewModelScope.launch(Dispatchers.IO) {
            productRepo.storeProduct(productList)
        }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                //start shimmer

            }

            Lifecycle.Event.ON_START -> {

            }

            Lifecycle.Event.ON_RESUME -> {
                pullProductsFromServer()
            }

            Lifecycle.Event.ON_PAUSE -> {

            }

            Lifecycle.Event.ON_STOP -> {

            }

            Lifecycle.Event.ON_DESTROY -> {

            }

            Lifecycle.Event.ON_ANY -> {


            }
        }
    }

}