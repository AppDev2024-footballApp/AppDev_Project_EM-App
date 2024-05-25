package com.example.fussball_em_2024_app.utils

import android.app.Application

class App : Application() {
    companion object {
        lateinit var instance: App private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}