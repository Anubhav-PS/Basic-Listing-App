package com.anubhav.swipetask.ui.activities

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.anubhav.swipetask.MainApplication
import com.anubhav.swipetask.models.Product
import com.anubhav.swipetask.repositories.ProductsRepository
import com.anubhav.swipetask.repositories.models.DataStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    application: Application,
    private val productsRepo: ProductsRepository,
    private val notificationBuilder: NotificationCompat.Builder,
    private val notificationManager: NotificationManagerCompat
) :
    AndroidViewModel(application) {

    private val TAG = "Main-Activity-View-Model"

    @RequiresPermission("android.permission.POST_NOTIFICATIONS")
    fun uploadProduct(product: Product, uri: Uri?) {
        viewModelScope.launch(Dispatchers.IO) {
            productsRepo.postProduct(
                product,
                uri
            ).collect {
                when (it.status) {
                    DataStatus.Status.Failed -> {
                        MainApplication._productUploadStatus.postValue(it)
                        notificationManager.notify(
                            1,
                            notificationBuilder.setContentTitle("Failed to post your product")
                                .setContentText(it.message.toString()).build()
                        )
                    }

                    DataStatus.Status.Loading -> {

                    }

                    DataStatus.Status.Success -> {
                        MainApplication._productUploadStatus.postValue(it)
                        val content = "\nProduct ID is : ${it.data?.productId}"
                        notificationManager.notify(
                            1,
                            notificationBuilder.setContentTitle("Product has been successfully posted")
                                .setContentText(content).build()
                        )
                    }
                }
            }
        }
    }

}