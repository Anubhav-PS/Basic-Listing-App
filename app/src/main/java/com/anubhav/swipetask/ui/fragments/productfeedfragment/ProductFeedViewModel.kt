package com.anubhav.swipetask.ui.fragments.productfeedfragment

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anubhav.swipetask.models.Product
import com.anubhav.swipetask.repositories.ProductsRepository
import com.anubhav.swipetask.repositories.models.DataStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductFeedViewModel(private val productRepo: ProductsRepository) : ViewModel(),
    LifecycleEventObserver {

    private val _productList = MutableLiveData<DataStatus<List<Product>>>()
    private val TAG = "Product-Feed-View-Model"
    val productList: LiveData<DataStatus<List<Product>>>
        get() = _productList

    private fun downloadProducts() = viewModelScope.launch(Dispatchers.IO) {
        productRepo.getAllProduct().collect {
            _productList.postValue(it)
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> {

            }

            Lifecycle.Event.ON_START -> {

            }

            Lifecycle.Event.ON_RESUME -> {
                downloadProducts()
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