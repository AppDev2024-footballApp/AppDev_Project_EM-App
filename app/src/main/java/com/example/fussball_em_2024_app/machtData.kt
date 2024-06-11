package com.example.fussball_em_2024_app

import Match

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
val retrofit = Retrofit.Builder()
    .baseUrl("https://api.openligadb.de/getmatchdata/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
val service = retrofit.create(ExchangeService::class.java)

interface ExchangeService {
    @GET("em/23")
    suspend fun getLatestMatch(): Match
}



