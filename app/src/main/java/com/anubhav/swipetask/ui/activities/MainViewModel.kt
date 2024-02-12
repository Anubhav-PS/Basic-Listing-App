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
import com.anubhav.swipetask.services.models.ProductUploadRequest
import com.anubhav.swipetask.services.models.ProductUploadResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val productRepo: ProductsRepository,
    private val notificationBuilder: NotificationCompat.Builder,
    private val notificationManager: NotificationManagerCompat,
    application: Application
) :
    AndroidViewModel(application), ProductUploadRequest.UploadCallback,
    ProductsRepository.ProductsResponseCallback {

    private val TAG = "Main-Activity-View-Model"
    private val _progressBarStatus: MutableLiveData<Int> = MutableLiveData()
    val progressBarStatus: LiveData<Int> = _progressBarStatus
    private val _productUploadStatus: MutableLiveData<DataStatus<ProductUploadResponse?>> =
        MutableLiveData()
    val productUploadStatus: LiveData<DataStatus<ProductUploadResponse?>> = _productUploadStatus

    fun uploadProduct(product: Product, uri: Uri?) {
        viewModelScope.launch(Dispatchers.IO) {
            productRepo.postProduct(
                product,
                uri,
                this@MainViewModel,
                this@MainViewModel
            )
        }
    }

    @RequiresPermission("android.permission.POST_NOTIFICATIONS")
    override fun onProgressUpdate(percentage: Int) {
        if (percentage == 0) notificationManager.notify(
            1,
            notificationBuilder.setContentTitle("Product image is being uploaded.")
                .setContentText("Your product image upload has started.").build()
        )
        if (percentage == 99) notificationManager.notify(
            1,
            notificationBuilder.setContentTitle("Product image has been uploaded.")
                .setContentText("Your product image upload has started.").build()
        )
    }

    @RequiresPermission("android.permission.POST_NOTIFICATIONS")
    override fun onStatusUpdated(dataStatus: DataStatus<ProductUploadResponse?>) {
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