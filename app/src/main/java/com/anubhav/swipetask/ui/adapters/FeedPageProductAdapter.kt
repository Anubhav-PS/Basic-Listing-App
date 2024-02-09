package com.anubhav.swipetask.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.anubhav.swipetask.R
import com.anubhav.swipetask.databinding.ProductItemBinding
import com.anubhav.swipetask.domains.Product

class FeedPageProductAdapter(private val productsList: List<Product>) :
    RecyclerView.Adapter<FeedPageProductAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ProductItemBinding =
            DataBindingUtil.inflate(inflater, R.layout.product_item, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(productsList[position])
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    inner class ViewHolder(private val binding: ProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(item: Product) {
            binding.product = item
            binding.executePendingBindings()
        }
    }
}