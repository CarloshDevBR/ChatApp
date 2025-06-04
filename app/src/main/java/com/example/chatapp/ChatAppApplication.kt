package com.example.chatapp

import android.app.Application
import com.example.chatapp.di.ApplicationModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ChatAppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        start()
    }

    private fun start() {
        startKoin {
            androidLogger()
            androidContext(this@ChatAppApplication)
            modules(ApplicationModules().load())
        }
    }
}