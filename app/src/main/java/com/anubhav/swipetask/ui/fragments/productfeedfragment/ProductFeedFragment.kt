package com.anubhav.swipetask.ui.fragments.productfeedfragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anubhav.swipetask.MainApplication
import com.anubhav.swipetask.R
import com.anubhav.swipetask.databinding.FragmentProductFeedBinding
import com.anubhav.swipetask.models.ProgressStatus
import com.anubhav.swipetask.repositories.models.DataStatus
import com.anubhav.swipetask.ui.activities.MainViewModel
import com.anubhav.swipetask.ui.adapters.FeedPageProductAdapter
import com.anubhav.swipetask.ui.fragments.uploadnewproductfragment.UploadNewProductFragment
import com.anubhav.swipetask.utils.ConnectivityListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class ProductFeedFragment : Fragment() {

    private lateinit var _binding: FragmentProductFeedBinding
    private val binding get() = _binding
    private lateinit var rootView: View
    private val mainViewModel: MainViewModel by inject()
    private val viewModel: ProductFeedViewModel by inject()
    private val connectivityListener: ConnectivityListener by inject()
    private var networkNotAvailable: Boolean = false
    private var freshListIsLoadedFromServer: Boolean = false
    private var isSearchViewOpened: Boolean = false
    private var progressStatus: ProgressStatus = ProgressStatus("", 0, false, false)
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

        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.product_feed_page_toolbar, menu)
                val search = menu.findItem(R.id.searchMenu)
                val searchView = search.actionView as? SearchView
                searchView?.queryHint = "Search for products.."
                searchView?.isSubmitButtonEnabled = true
                searchView?.maxWidth = Integer.MAX_VALUE
                searchView?.setOnCloseListener {
                    isSearchViewOpened = false
                    binding.productFilterNSortConstraintLayout.visibility = View.VISIBLE
                    binding.addProductExtendedFab.visibility = View.VISIBLE
                    false
                }
                searchView?.setOnSearchClickListener {
                    isSearchViewOpened = true
                    binding.productFilterNSortConstraintLayout.visibility = View.GONE
                }
                searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        query?.apply {
                            val searchQuery = "%$this%"
                            viewModel.searchProduct(searchQuery)
                        }
                        return true
                    }

                    override fun onQueryTextChange(query: String?): Boolean {
                        query?.apply {
                            val searchQuery = "%$this%"
                            viewModel.searchProduct(searchQuery)
                        }
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        }, viewLifecycleOwner)

        binding.productsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.productList.observe(viewLifecycleOwner) { list ->
            lifecycleScope.launch {
                delay(6000) //simulate slow network
                list?.apply {
                    binding.productFilterNSortConstraintLayout.visibility = View.VISIBLE
                    binding.productsRecyclerView.visibility = View.VISIBLE
                    binding.addProductExtendedFab.visibility = View.VISIBLE
                    binding.productsShimmerLayout.stopShimmer()
                    binding.productsShimmerLayout.visibility = View.GONE
                    binding.productsRecyclerView.adapter = FeedPageProductAdapter(this)
                }
            }
        }
        viewModel.productListFromNetworkStatus.observe(viewLifecycleOwner) {
            when (it) {
                DataStatus.Status.Failed -> {
                    if (networkNotAvailable) return@observe
                    //only show snack-bar if there was error fetching data while network is available
                    freshListIsLoadedFromServer = false
                    val snackBar = Snackbar.make(
                        binding.productsCoordinatorLayout,
                        "Oops! We couldn't load the data. Please try again.",
                        Snackbar.LENGTH_INDEFINITE
                    )
                    snackBar.setAction("Retry") {
                        viewModel.pullProductsFromServer()
                    }
                    snackBar.setActionTextColor(Color.parseColor("#2E84D2"))
                    snackBar.setBackgroundTint(Color.parseColor("#FFFFFF"))
                    snackBar.setTextColor(Color.parseColor("#000000"))
                    snackBar.show()
                }

                DataStatus.Status.Loading -> {
                    binding.productFilterNSortConstraintLayout.visibility = View.GONE
                    binding.productsRecyclerView.visibility = View.GONE
                    binding.addProductExtendedFab.visibility = View.GONE
                    binding.productsShimmerLayout.visibility = View.VISIBLE
                    binding.productsShimmerLayout.startShimmer()
                }

                DataStatus.Status.Success -> {
                    freshListIsLoadedFromServer = true
                }
            }
        }
        binding.productsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        if (isSearchViewOpened) return
                        binding.addProductExtendedFab.show()
                        binding.productFilterNSortConstraintLayout.visibility = View.VISIBLE
                    }

                    else -> {
                        binding.addProductExtendedFab.hide()
                        binding.productFilterNSortConstraintLayout.visibility = View.GONE
                    }
                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        connectivityListener.observe(viewLifecycleOwner) { isAvailable ->
            when (isAvailable) {
                true -> {
                    //if network is back online (check if network was down prior to this and if yes , then show snack-bar).
                    // This check is done in other to prevent the showing snack-bar in the app start
                    if (networkNotAvailable) {
                        networkNotAvailable = false
                        val snackBar = Snackbar.make(
                            binding.productsCoordinatorLayout,
                            "Yay! You're back online.", Snackbar.LENGTH_LONG
                        )
                        snackBar.setBackgroundTint(Color.parseColor("#4CAF50"))
                        snackBar.setTextColor(Color.parseColor("#FFFFFF"))
                        snackBar.show()
                        if (!freshListIsLoadedFromServer) {
                            // if network was down before opening the app and local entries was empty , then pull from server since network is available now
                            viewModel.pullProductsFromServer()
                        }
                        return@observe
                    }
                }

                false -> {
                    networkNotAvailable = true
                    val snackBar = Snackbar.make(
                        binding.productsCoordinatorLayout,
                        "Oops! Looks like you're offline.",
                        Snackbar.LENGTH_LONG
                    )
                    snackBar.setBackgroundTint(Color.parseColor("#B00020"))
                    snackBar.setTextColor(Color.parseColor("#FFFFFF"))
                    snackBar.show()
                }
            }
        }
        connectivityListener.isNetworkAvailable().apply {
            //To check if the internet was down even before the app launch
            if (!this) {
                networkNotAvailable = true
                val snackBar = Snackbar.make(
                    binding.productsCoordinatorLayout,
                    "Oops! Looks like you're offline.",
                    Snackbar.LENGTH_LONG
                )
                snackBar.setBackgroundTint(Color.parseColor("#B00020"))
                snackBar.setTextColor(Color.parseColor("#FFFFFF"))
                snackBar.show()
            }
        }
        viewModel.queriedProduct.observe(viewLifecycleOwner) {
            when (it.status) {
                DataStatus.Status.Failed -> {
                    val snackBar = Snackbar.make(
                        binding.productsCoordinatorLayout,
                        "We encountered an issue while processing your query.",
                        Snackbar.LENGTH_LONG
                    )
                    snackBar.setBackgroundTint(Color.parseColor("#FFFFFF"))
                    snackBar.setTextColor(Color.parseColor("#000000"))
                    snackBar.show()
                }

                DataStatus.Status.Loading -> {
                    binding.productsRecyclerView.visibility = View.GONE
                    binding.addProductExtendedFab.visibility = View.GONE
                    binding.productsShimmerLayout.visibility = View.VISIBLE
                    binding.productsShimmerLayout.startShimmer()
                }

                DataStatus.Status.Success -> {
                    binding.productsRecyclerView.visibility = View.VISIBLE
                    binding.addProductExtendedFab.visibility = View.VISIBLE
                    binding.productsShimmerLayout.stopShimmer()
                    binding.productsShimmerLayout.visibility = View.GONE
                    it.data?.apply {
                        if (this.isNotEmpty()) {
                            binding.productsRecyclerView.adapter =
                                FeedPageProductAdapter(it.data.toList())
                            return@observe
                        }
                        val snackBar = Snackbar.make(
                            binding.productsCoordinatorLayout,
                            "Sorry, we couldn't find any results matching your search.",
                            Snackbar.LENGTH_LONG
                        )
                        snackBar.setBackgroundTint(Color.parseColor("#000000"))
                        snackBar.setTextColor(Color.parseColor("#FFFFFF"))
                        snackBar.show()
                    }
                }
            }
        }
        binding.addProductExtendedFab.setOnClickListener {
            val uploadNewProductFragment = UploadNewProductFragment()
            uploadNewProductFragment.show(
                parentFragmentManager, UploadNewProductFragment.TAG
            )
        }
        MainApplication.productUploadStatus.observe(viewLifecycleOwner) {
            when (it.status) {
                DataStatus.Status.Failed -> {

                }

                DataStatus.Status.Loading -> {

                }

                DataStatus.Status.Success -> {
                    viewModel.pullProductsFromServer()
                }
            }
        }
    }

}