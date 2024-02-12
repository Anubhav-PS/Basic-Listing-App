package com.anubhav.swipetask.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.anubhav.swipetask.R
import com.anubhav.swipetask.databinding.ActivityMainBinding
import com.anubhav.swipetask.models.ProgressStatus
import com.anubhav.swipetask.repositories.ProductsRepository
import com.anubhav.swipetask.repositories.models.DataStatus
import com.anubhav.swipetask.services.models.ProductUploadRequest
import com.anubhav.swipetask.services.models.ProductUploadResponse
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), ProductUploadRequest.UploadCallback,
    ProductsRepository.ProductsResponseCallback {

    private lateinit var _binding: ActivityMainBinding
    private val binding get() = _binding
    private val viewModel: MainViewModel by viewModel()
    private val TAG = "Main-View-Model"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        setSupportActionBar(binding.mainToolbar)
        binding.uploadProgress = ProgressStatus("", 0, false, false)
        viewModel.progressBarStatus.observe(this) {
            Log.i(TAG, "Its uploading in main activity $it")
        }
        viewModel.productUploadStatus.observe(this) {
        }
    }

    override fun onStatusUpdated(dataStatus: DataStatus<ProductUploadResponse?>) {
        when (dataStatus.status) {
            DataStatus.Status.Failed -> {
                binding.uploadProgress =
                    ProgressStatus(dataStatus.message.toString(), 0, true, true)
                MaterialAlertDialogBuilder(
                    this@MainActivity,
                    com.google.android.material.R.style.MaterialAlertDialog_Material3
                )
                    .setTitle("Failed to upload")
                    .setMessage(dataStatus.message)
                    .setPositiveButton("Ok") { _, _ ->
                        binding.uploadProgress = ProgressStatus("", 0, false, false)
                    }
                    .setCancelable(false)
                    .show()
            }

            DataStatus.Status.Loading -> {
                binding.uploadProgress =
                    ProgressStatus(dataStatus.message.toString(), 0, true, true)
            }

            DataStatus.Status.Success -> {
                Log.i(TAG, "Its uploaded in main")
                binding.uploadProgress =
                    ProgressStatus(dataStatus.message.toString(), 0, true, true)
                MaterialAlertDialogBuilder(
                    this@MainActivity,
                    com.google.android.material.R.style.MaterialAlertDialog_Material3
                )
                    .setTitle("Product Uploaded Successfully")
                    .setMessage("Product ID : ${dataStatus.data?.productId}")
                    .setPositiveButton("Ok") { _, _ ->
                        binding.uploadProgress = ProgressStatus("", 0, false, false)
                    }
                    .setCancelable(false)
                    .show()
            }
        }
    }

    override fun onProgressUpdate(percentage: Int) {
        Log.i(TAG, "Its uploading in main $percentage")
        binding.uploadProgress = ProgressStatus("Uploading in progress", percentage, false, true)
    }

}