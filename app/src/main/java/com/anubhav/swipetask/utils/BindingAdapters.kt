package com.anubhav.swipetask.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import com.anubhav.swipetask.R
import java.text.DecimalFormat

@BindingAdapter("loadImageFromUrl")
fun ImageView.loadImageFromUrl(imageUrl: String) {
    if (imageUrl == "") {
        this.load(R.drawable.product_item_default_image)
        return
    }
    this.load(imageUrl) {
        placeholder(R.drawable.product_item_default_image)
    }
}

@BindingAdapter("priceInINR")
fun TextView.priceInINR(price: Double) {
    val formatter = DecimalFormat("#,##,##0.00")
    val formattedValue = formatter.format(price)
    this.text = "Price  ₹ ".plus(formattedValue)
}