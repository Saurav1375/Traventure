package com.example.tripapplication

import android.app.Application
import com.example.tripapplication.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TripApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TripApplication)
            androidLogger()
            modules(appModule)
        }
    }
}