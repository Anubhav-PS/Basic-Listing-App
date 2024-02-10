package com.anubhav.swipetask.ui.di

import com.anubhav.swipetask.ui.fragments.productfeedfragment.ProductFeedViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel() { ProductFeedViewModel(get()) }
}