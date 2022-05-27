package com.wenger.location_viewer.app

import android.app.Application
import com.wenger.common.di.firebaseModule
import com.wenger.common.di.logSignUpModule
import com.wenger.location_viewer.di.appModule
import com.wenger.location_viewer.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class CheckerApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@CheckerApplication)
            modules(listOf(appModule, firebaseModule, repositoryModule, logSignUpModule))
        }
    }
}