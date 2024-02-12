package com.anubhav.swipetask.ui.fragments.uploadnewproductfragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.anubhav.swipetask.R
import com.anubhav.swipetask.databinding.FragmentUploadNewProductBinding
import com.anubhav.swipetask.models.Product
import com.anubhav.swipetask.ui.activities.MainViewModel
import com.anubhav.swipetask.ui.fragments.productfeedfragment.ProductFeedViewModel
import com.anubhav.swipetask.utils.ConnectivityListener
import com.anubhav.swipetask.utils.parseStringToPrice
import com.anubhav.swipetask.utils.validatePriceFormat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.android.ext.android.inject

class UploadNewProductFragment : BottomSheetDialogFragment() {

    private lateinit var _binding: FragmentUploadNewProductBinding
    private val binding get() = _binding
    private lateinit var rootView: View
    private val mainViewModel : MainViewModel by inject()
    private val connectivityListener: ConnectivityListener by inject()
    private var networkNotAvailable: Boolean = false
    private var selectedCategoryPosition: Int = 0
    private var inputProductPrice: Double = 0.0
    private var inputTax: Double = 0.0
    private var inputProductType: String = ""
    private var inputProductName: String = ""
    private var selectedImageUri: Uri? = null

    companion object {

        const val TAG = "Upload-New-Product-Fragment"
    }

    private val getActionIntentResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            selectedImageUri = it.data?.data
            binding.uploadProductImageTextViewLabel.text = "Image Uploaded. Tap to change."
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUploadNewProductBinding.inflate(inflater, container, false)
        rootView = binding.root
        binding.lifecycleOwner = viewLifecycleOwner
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.uploadProductButton.setOnClickListener {
            if (!networkNotAvailable) {
                if (validateInput()) {
                    val product =
                        Product("", inputProductPrice, inputProductName, inputProductType, inputTax)
                    if (selectedImageUri == null) {
                        MaterialAlertDialogBuilder(
                            requireContext(),
                            com.google.android.material.R.style.MaterialAlertDialog_Material3
                        )
                            .setTitle("Image not attached.")
                            .setMessage("Would you like to proceed without attaching image?")
                            .setPositiveButton("Proceed") { _, _ ->
                                this@UploadNewProductFragment.dismiss()
                                mainViewModel.uploadProduct(product, selectedImageUri)
                            }
                            .setNegativeButton("Abort") { _, _ ->
                                dismiss()
                            }
                            .setCancelable(false)
                            .show()
                        return@setOnClickListener
                    }
                    this@UploadNewProductFragment.dismiss()
                    mainViewModel.uploadProduct(product, selectedImageUri)
                }
            }
        }
        val productCategories = resources.getStringArray(R.array.product_categories_array)
        val productCategoryDropDownAdapter = ArrayAdapter(
            requireContext(),
            R.layout.drop_down_product_category_item,
            productCategories
        )
        binding.productTypeAutoCompleteTextView.setAdapter(productCategoryDropDownAdapter)
        binding.productTypeAutoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            run {
                binding.productTypeDropDownMenu.error = null
                selectedCategoryPosition = position
            }
        }
        binding.productPriceTextInputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.productPriceTextInputLayout.error = null
                val input = binding.productPriceTextInputEditText.text.toString()
                validatePriceFormat(
                    input,
                    binding.productPriceTextInputEditText,
                    binding.productPriceTextInputLayout,
                    false
                )
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        binding.productTaxTextInputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.productTaxTextInputLayout.error = null
                val input = binding.productTaxTextInputEditText.text.toString()
                validatePriceFormat(
                    input,
                    binding.productTaxTextInputEditText,
                    binding.productTaxTextInputLayout,
                    true
                )
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        binding.uploadProductImageTextViewLabel.setOnClickListener {
            pickImageFromGallery()
        }
        binding.cancelButton.setOnClickListener {
            this@UploadNewProductFragment.dismiss()
        }
        connectivityListener.observe(viewLifecycleOwner) { isAvailable ->
            networkNotAvailable = when (isAvailable) {
                true -> {
                    false
                }

                false -> {
                    true
                }
            }
        }
        connectivityListener.isNetworkAvailable().apply {
            if (!this) {
                networkNotAvailable = true
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeType = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeType)
        }
        getActionIntentResult.launch(intent)
    }

    private fun validateInput(): Boolean {
        if (binding.productNameTextInputEditText.text.toString().isBlank()) {
            binding.productNameTextInputLayout.error = "Product name cannot be empty"
            return false
        }
        binding.productNameTextInputLayout.error = null
        if (binding.productPriceTextInputEditText.text.toString().isBlank()) {
            binding.productPriceTextInputLayout.error = "Mention price"
            return false
        }
        binding.productPriceTextInputLayout.error = null
        if (binding.productTaxTextInputEditText.text.toString().isBlank()) {
            binding.productTaxTextInputLayout.error = "Mention tax"
            return false
        }
        binding.productTaxTextInputLayout.error = null
        if (selectedCategoryPosition == 0) {
            binding.productTypeDropDownMenu.error = "Product category is needed."
            return false
        }
        binding.productTypeDropDownMenu.error = null
        val price = binding.productPriceTextInputEditText.text.toString()
        if (!price.matches(Regex("""^[1-9]\d*(\.\d{0,2})?\)?$"""))) {
            binding.productPriceTextInputLayout.error = "Invalid"
            return false
        }
        val tax = binding.productTaxTextInputEditText.text.toString()
        if (!tax.matches(Regex("""^(0|[1-9]\d*)(\.\d{0,2})?\)?$"""))) {
            binding.productTaxTextInputLayout.error = "Invalid"
            return false
        }
        val parsedPrice = price.parseStringToPrice()
        if (parsedPrice == null) {
            binding.productPriceTextInputLayout.error = "Invalid"
            return false
        }
        val parsedTax = tax.parseStringToPrice()
        if (parsedTax == null) {
            binding.productTaxTextInputLayout.error = "Invalid"
            return false
        }
        inputProductName = binding.productNameTextInputEditText.text.toString()
        inputProductPrice = parsedPrice
        inputTax = parsedTax
        inputProductType =
            resources.getStringArray(R.array.product_categories_array)[selectedCategoryPosition]
        return true
    }

}