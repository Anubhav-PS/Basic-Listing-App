package com.anubhav.swipetask.ui.fragments.productfeedfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.anubhav.swipetask.databinding.FragmentProductFeedBinding
import com.anubhav.swipetask.repositories.models.DataStatus
import com.anubhav.swipetask.ui.adapters.FeedPageProductAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class ProductFeedFragment : Fragment() {

    private var _binding: FragmentProductFeedBinding? = null
    private val binding get() = _binding!!
    private lateinit var rootView: View
    private val viewModel: ProductFeedViewModel by inject()
    val TAG = "Products-Feed-Fragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductFeedBinding.inflate(inflater, container, false)
        rootView = binding.root
        binding.lifecycleOwner = viewLifecycleOwner
        lifecycle.addObserver(viewModel)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.productsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.productList.observe(viewLifecycleOwner) { list ->
            lifecycleScope.launch {
                delay(6000) //simulate slow network
                list?.apply {
                    binding.productFilterNSortConstraintLayout.visibility = View.VISIBLE
                    binding.productsRecyclerView.visibility = View.VISIBLE
                    binding.productsShimmerLayout.stopShimmer()
                    binding.productsShimmerLayout.visibility = View.GONE
                    binding.productsShimmerLayout.stopShimmer()
                    binding.productsRecyclerView.adapter = FeedPageProductAdapter(this)
                }
            }
        }

        viewModel.productListFromNetworkStatus.observe(viewLifecycleOwner) {
            when (it) {
                DataStatus.Status.Failed -> {
                    binding.filterProductsTextLabel.visibility = View.GONE
                }

                DataStatus.Status.Loading -> {
                    binding.productFilterNSortConstraintLayout.visibility = View.GONE
                    binding.productsRecyclerView.visibility = View.GONE
                    binding.productsShimmerLayout.startShimmer()
                }

                DataStatus.Status.Success -> {

                }
            }
        }

    }

}