package com.example.fussball_em_2024_app

import com.example.fussball_em_2024_app.model.League
import com.example.fussball_em_2024_app.model.Match
import com.example.fussball_em_2024_app.model.Team
import com.example.fussball_em_2024_app.model.TeamInfo
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


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
    @GET("getavailableleagues")
    suspend fun getAllLeagues(): List<League>

    @GET("getmatchdata/{leagueShortcut}/{leagueSeason}/")
    suspend fun getLatestMatch(@Path("leagueShortcut") leagueShortcut: String, @Path("leagueSeason") leagueSeason: String): List<Match>

    @GET("getmatchdata/{id}")
    suspend fun getMatch(@Path("id") id: Int):Match?

    @GET("getnextmatchbyleagueshortcut/{leagueShortcut}")
    suspend fun getNextMatch(@Path("leagueShortcut") leagueShortcut: String):Match

    @GET("getlastmatchbyleagueshortcut/{leagueShortcut}")
    suspend fun getLastMatch(@Path("leagueShortcut") leagueShortcut: String):Match

    @GET("getavailableteams/{leagueShortcut}/{leagueSeason}")
    suspend fun getTeams(@Path("leagueShortcut") leagueShortcut: String, @Path("leagueSeason") leagueSeason: String):List<Team>

    @GET("getbltable/{leagueShortcut}/{leagueSeason}")
    suspend fun getTeamsDetails(@Path("leagueShortcut") leagueShortcut: String, @Path("leagueSeason") leagueSeason: String):List<TeamInfo>

    @GET("getnextmatchbyleagueteam/{leagueId}/{id}")
    suspend fun getNextMatchByTeam(@Path("leagueId")leagueId: Int, @Path("id") id: Int):Match

    @GET("getlastmatchbyleagueteam/{leagueId}/{id}")
    suspend fun getLastMatchByTeam(@Path("leagueId")leagueId: Int, @Path("id")id: Int):Match
    @GET("getmatchesbyteam/{teamName}/{pastWeeks}/0")
    suspend fun getLastMatchesByTeam(@Path("teamName") teamName: String, @Path("pastWeeks") pastWeeks: Int): List<Match>
}





