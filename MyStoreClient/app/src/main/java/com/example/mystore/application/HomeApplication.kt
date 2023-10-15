package com.example.mystore.application

import android.app.Application
import android.content.Context

class HomeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        appContext = applicationContext
    }

    companion object {
        @JvmStatic
        private lateinit var instance: HomeApplication

        @JvmStatic
        private lateinit var appContext: Context

        fun getInstance(): HomeApplication {
            return instance
        }

        fun getAppContext(): Context {
            return appContext
        }
    }
}