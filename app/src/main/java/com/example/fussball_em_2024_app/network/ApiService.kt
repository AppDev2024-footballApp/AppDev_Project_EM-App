package com.example.fussball_em_2024_app.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(httpLoggingInterceptor)
    .build()
val gson = GsonBuilder()
    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss") // The format should correspond to the date string
    .create()

val retrofit = Retrofit.Builder()
    .baseUrl("https://api.openligadb.de/")
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create(gson))
    .build()


val apiService: ApiRoutes = retrofit.create(ApiRoutes::class.java)