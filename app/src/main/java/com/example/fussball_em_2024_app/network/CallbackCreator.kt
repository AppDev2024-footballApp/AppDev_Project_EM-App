package com.example.fussball_em_2024_app.network

import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class CallbackCreator {
    fun createCallback(onFailure: () -> Unit, onResponse: (response: Response) -> Unit): Callback {
        return object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onFailure()
            }

            override fun onResponse(call: Call, response: Response) {
                onResponse(response)
            }
        }
    }
}