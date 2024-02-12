package com.anubhav.swipetask.ui.activities

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import com.anubhav.swipetask.MainApplication
import com.anubhav.swipetask.R
import com.anubhav.swipetask.databinding.ActivityMainBinding
import com.anubhav.swipetask.repositories.models.DataStatus
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityMainBinding
    private val binding get() = _binding
    private val viewModel: MainViewModel by inject()
    private val TAG = "Main-View-Model"
    private val getActionIntentResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
    }
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                showRequestPermissionRationale()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        setSupportActionBar(binding.mainToolbar)
        requestNotificationPermission()
        MainApplication.productUploadStatus.observe(this) {
            when (it.status) {
                DataStatus.Status.Failed -> {
                    it.data?.apply {
                        val title = this.message
                        val message = "We couldn't upload your product. Try again later."
                        MaterialAlertDialogBuilder(
                            this@MainActivity,
                            com.google.android.material.R.style.MaterialAlertDialog_Material3
                        )
                            .setTitle(title)
                            .setMessage(message)
                            .setPositiveButton("OK") { _, _ -> }
                            .setCancelable(true)
                            .show()
                    }
                }

                DataStatus.Status.Loading -> {

                }

                DataStatus.Status.Success -> {
                    it.data?.apply {
                        val title = this.message
                        val productName = this.productDetails.productName
                        val productId = this.productId
                        val message =
                            productName.plus(" has been uploaded and assigned the product id as ")
                                .plus(productId)
                        MaterialAlertDialogBuilder(
                            this@MainActivity,
                            com.google.android.material.R.style.MaterialAlertDialog_Material3
                        )
                            .setTitle(title)
                            .setMessage(message)
                            .setPositiveButton("OK") { _, _ -> }
                            .setCancelable(true)
                            .show()
                    }
                }
            }
        }
    }

    private fun requestNotificationPermission() {
        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    private fun showRequestPermissionRationale() {
        MaterialAlertDialogBuilder(
            this,
            com.google.android.material.R.style.MaterialAlertDialog_Material3
        )
            .setTitle("Notification blocked")
            .setMessage("Notification permission is needed to timely update you about any product upload status")
            .setPositiveButton("Ok") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                val uri: Uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                getActionIntentResult.launch(intent)
            }
            .setCancelable(false)
            .show()
    }
}