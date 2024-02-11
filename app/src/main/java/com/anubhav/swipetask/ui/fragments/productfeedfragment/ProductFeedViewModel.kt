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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProductFeedViewModel(private val productRepo: ProductsRepository) : ViewModel(),
    LifecycleEventObserver {

    private val TAG = "Product-Feed-View-Model"
    val productList: LiveData<List<Product>> = productRepo.allProducts.asLiveData()
    private val _productListFromNetworkStatus: MutableLiveData<DataStatus.Status> =
        MutableLiveData()
    var productListFromNetworkStatus: LiveData<DataStatus.Status> = _productListFromNetworkStatus

    private val _queriedProducts: MutableLiveData<DataStatus<List<Product>>> = MutableLiveData()
    val queriedProduct : LiveData<DataStatus<List<Product>>> = _queriedProducts

    fun pullProductsFromServer() {
        viewModelScope.launch(Dispatchers.IO) {
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
                            saveProductsToDB(this)
                        }
                    }
                }
            }
        }
    }

    private fun saveProductsToDB(productList: List<Product>) {
        viewModelScope.launch(Dispatchers.IO) {
            productRepo.storeProduct(productList)
        }
    }

    fun searchProduct(query:String){
        viewModelScope.launch (Dispatchers.IO){
            productRepo.searchProduct(query).collect{
                _queriedProducts.postValue(it)
            }
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_CREATE) {
            //make a call to the server at every app start
            pullProductsFromServer()
        }
    }

}