package com.example.fussball_em_2024_app

import com.example.fussball_em_2024_app.model.Match
import com.example.fussball_em_2024_app.model.Team
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
    .build()
val gson = GsonBuilder()
    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss") // The format should correspond to the date string
    .create()

val retrofit = Retrofit.Builder()
    .baseUrl("https://api.openligadb.de/")
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create(gson))
    .build()


val matchService = retrofit.create(ApiService::class.java)


interface ApiService {
    @GET("getmatchdata/em/2024/")
    suspend fun getLatestMatch(): List<Match>

    @GET("getnextmatchbyleagueshortcut/EM")
    suspend fun getNextMatch():Match

    @GET("getlastmatchbyleagueshortcut/EM")
    suspend fun getLastMatch():Match

    @GET("getavailableteams/EM/2024")
    suspend fun getTeams():List<Team>
}



