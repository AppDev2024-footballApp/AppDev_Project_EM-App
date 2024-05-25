package com.example.fussball_em_2024_app.network

import com.example.fussball_em_2024_app.R
import com.example.fussball_em_2024_app.utils.Strings
import okhttp3.Callback
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request

object HttpClient {
    private var client: OkHttpClient = OkHttpClient()
    private var baseUrl: String = Strings.get(R.string.openLiga_url)

    fun get(url: String, callback: Callback) {
        val requestUrl = makeRequestUrl(url)
        val request = Request.Builder()
            .url(requestUrl)
            .build()

        client.newCall(request).enqueue(callback)
    }

    @Throws(IllegalArgumentException::class)
    private fun makeRequestUrl(path: String): String {
        return baseUrl.toHttpUrl().newBuilder().addPathSegments(path.trimStart('/')).build().toString()
    }

    fun resetClient(baseUrl: String = Strings.get(R.string.openLiga_url)) {
        client = OkHttpClient()
        this.baseUrl = baseUrl
    }

}