package com.example.fussball_em_2024_app

import com.example.fussball_em_2024_app.model.Match
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(httpLoggingInterceptor)
    // ... andere Interceptors oder Einstellungen falls benötigt
    .build()
val gson = GsonBuilder()
    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss") // Das Format muss Ihrem Date-String entsprechen
    .create()

val retrofit = Retrofit.Builder()
    .baseUrl("https://api.openligadb.de/getmatchdata/")
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create(gson)) // Verwenden Sie das `gson`-Objekt hier
    .build()


val matchService = retrofit.create(ApiService::class.java)


interface ApiService {
    @GET("em2024-emem/2002/1")
    suspend fun getLatestMatch(): List<Match>
}



