package com.anubhav.swipetask.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.anubhav.swipetask.R
import com.anubhav.swipetask.databinding.ActivityMainBinding
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityMainBinding
    private val binding get() = _binding
    private val viewModel: MainViewModel by inject()
    private val TAG = "Main-View-Model"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        setSupportActionBar(binding.mainToolbar)
        lifecycle.addObserver(viewModel)
    }

}