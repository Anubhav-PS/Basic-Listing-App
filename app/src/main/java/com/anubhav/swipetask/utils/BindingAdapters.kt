package com.anubhav.swipetask.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import com.anubhav.swipetask.R

@BindingAdapter("loadImageFromUrl")
fun ImageView.loadImageFromUrl(imageUrl: String) {
    if (imageUrl=="") {
        this.load(R.drawable.product_item_default_image)
        return
    }
    this.load(imageUrl) {
        placeholder(R.drawable.product_item_default_image)
    }
}