package com.anubhav.swipetask.ui.fragments.productfeedfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.anubhav.swipetask.databinding.FragmentProductFeedBinding
import com.anubhav.swipetask.repositories.models.DataStatus
import com.anubhav.swipetask.ui.adapters.FeedPageProductAdapter
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

        viewModel.productList.observe(viewLifecycleOwner) {
            it.apply {
                //close shimmer
                binding.productsRecyclerView.adapter = FeedPageProductAdapter(this)
            }
        }

        viewModel.productListFromNetworkStatus.observe(viewLifecycleOwner){
            if (it is DataStatus.Status.Failed){
                //show error
            }
        }

    }

}