package com.example.fussball_em_2024_app.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.fussball_em_2024_app.utils.LightSensorManager

class LightSensorViewModel(application: Application) : AndroidViewModel(application) {
    private val lightSensorManager = LightSensorManager(application)

    val lightData: LiveData<Float> = lightSensorManager.lightData

    override fun onCleared() {
        super.onCleared()
        lightSensorManager.unregister()
    }
}