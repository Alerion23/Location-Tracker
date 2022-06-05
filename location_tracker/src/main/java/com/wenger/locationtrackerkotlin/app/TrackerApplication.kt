package com.wenger.locationtrackerkotlin.app

import android.app.Application
import com.wenger.common.di.databaseModule
import com.wenger.common.di.authModule
import com.wenger.locationtrackerkotlin.di.appModule
import com.wenger.locationtrackerkotlin.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TrackerApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@TrackerApplication)
            modules(listOf(appModule, databaseModule, repositoryModule, authModule))
        }
    }

}