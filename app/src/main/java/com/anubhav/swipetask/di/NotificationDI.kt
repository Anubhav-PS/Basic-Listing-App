package com.anubhav.swipetask.di

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.anubhav.swipetask.R

fun provideNotificationBuilder(application: Application) =
    NotificationCompat.Builder(application.applicationContext, "Product Upload Channel")
        .setSmallIcon(R.drawable.baseline_auto_awesome_24)
        .setPriority(NotificationCompat.PRIORITY_MAX)

fun provideNotificationManager(application: Application): NotificationManagerCompat {
    val notificationManager = NotificationManagerCompat.from(application.applicationContext)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            "Product Upload Channel",
            "Product Upload Channel",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)
    }
    return notificationManager
}