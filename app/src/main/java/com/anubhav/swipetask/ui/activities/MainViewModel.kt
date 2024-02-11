package com.anubhav.swipetask.ui.activities

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

class MainViewModel(application: Application) : AndroidViewModel(application),
    LifecycleEventObserver {



    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> {

            }

            Lifecycle.Event.ON_START -> {

            }

            Lifecycle.Event.ON_RESUME -> {

            }

            Lifecycle.Event.ON_PAUSE -> {

            }

            Lifecycle.Event.ON_STOP -> {

            }

            Lifecycle.Event.ON_DESTROY -> {

            }

            Lifecycle.Event.ON_ANY -> {

            }
        }
    }


}