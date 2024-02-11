package com.anubhav.swipetask.di

import com.anubhav.swipetask.database.di.databaseModule
import com.anubhav.swipetask.repositories.di.repositoryModule
import com.anubhav.swipetask.services.di.serviceModule
import com.anubhav.swipetask.ui.di.viewModelModule
import com.anubhav.swipetask.utils.di.utilsModule
import org.koin.dsl.module

val appModule = module {
    includes(
        databaseModule,
        serviceModule,
        repositoryModule,
        viewModelModule,
        utilsModule
    )
}