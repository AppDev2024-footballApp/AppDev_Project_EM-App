package com.example.fussball_em_2024_app

import com.example.fussball_em_2024_app.model.MatchResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


val retrofit = Retrofit.Builder()
    .baseUrl("https://api.openligadb.de/getmatchdata/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
val matchService = retrofit.create(ApiService::class.java)

interface ApiService {
    @GET("em/23")
    suspend fun getLatestMatch(): MatchResponse
}



