package com.anubhav.swipetask.ui.di

import com.anubhav.swipetask.ui.activities.MainViewModel
import com.anubhav.swipetask.ui.fragments.productfeedfragment.ProductFeedViewModel
import com.anubhav.swipetask.ui.fragments.uploadnewproductfragment.UploadNewProductViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(get(), get(), get(), androidApplication()) }
    viewModel { ProductFeedViewModel(get()) }
    viewModel { UploadNewProductViewModel() }
}