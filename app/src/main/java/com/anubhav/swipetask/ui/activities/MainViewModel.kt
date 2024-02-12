package com.anubhav.swipetask.ui.activities

import android.app.Application
import android.net.Uri
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.anubhav.swipetask.models.Product
import com.anubhav.swipetask.repositories.ProductsRepository
import com.anubhav.swipetask.repositories.models.DataStatus
import com.anubhav.swipetask.services.models.ProductUploadResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    application: Application,
    private val productsRepo: ProductsRepository,
    private val notificationBuilder: NotificationCompat.Builder,
    private val notificationManager: NotificationManagerCompat
) :
    AndroidViewModel(application),
    ProductsRepository.ProductsResponseCallback {

    private val TAG = "Main-Activity-View-Model"
    private val _productUploadStatus: MutableLiveData<DataStatus<ProductUploadResponse?>> =
        MutableLiveData()
    val productUploadStatus: LiveData<DataStatus<ProductUploadResponse?>> = _productUploadStatus

    fun uploadProduct(product: Product, uri: Uri?) {
        viewModelScope.launch(Dispatchers.IO) {
            productsRepo.postProduct(
                product,
                uri,
                this@MainViewModel
            )
        }
    }

    @RequiresPermission("android.permission.POST_NOTIFICATIONS")
    override fun onStatusUpdated(dataStatus: DataStatus<ProductUploadResponse?>) {
        _productUploadStatus.postValue(dataStatus)
        when (dataStatus.status) {
            DataStatus.Status.Failed -> {
                notificationManager.notify(
                    1,
                    notificationBuilder.setContentTitle("Failed to post your product")
                        .setContentText(dataStatus.message.toString()).build()
                )
            }

            DataStatus.Status.Loading -> {

            }

            DataStatus.Status.Success -> {
                val content = "\nProduct ID is : ${dataStatus.data?.productId}"
                notificationManager.notify(
                    1,
                    notificationBuilder.setContentTitle("Product has been successfully posted")
                        .setContentText(content).build()
                )
            }
        }
    }


}