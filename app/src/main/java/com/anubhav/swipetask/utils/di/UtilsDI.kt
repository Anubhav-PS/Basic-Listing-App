package com.anubhav.swipetask.utils.di

import android.app.Application
import com.anubhav.swipetask.utils.ConnectivityListener

fun provideConnectivityListener(application: Application) = ConnectivityListener(application)