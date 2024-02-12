package com.anubhav.swipetask

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.anubhav.swipetask.di.appModule
import com.anubhav.swipetask.repositories.models.DataStatus
import com.anubhav.swipetask.services.models.ProductUploadResponse
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(appModule)
        }

    }

    companion object{
        val _productUploadStatus: MutableLiveData<DataStatus<ProductUploadResponse>> =
            MutableLiveData()
        val productUploadStatus: LiveData<DataStatus<ProductUploadResponse>> = _productUploadStatus
    }
}