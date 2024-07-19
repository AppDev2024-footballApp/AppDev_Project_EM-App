package com.example.fussball_em_2024_app.utils

import android.content.Context
import com.example.fussball_em_2024_app.model.League

class StoreLeague {

    private val prefKey: String = "CurrentLeague"

    fun saveCurrentLeague(league: League, context: Context){
        val sharedPreferences = context.getSharedPreferences(prefKey, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putInt("leagueId", league.leagueId)
        editor.putString("leagueShortcut", league.leagueShortcut)
        editor.putString("leagueSeason", league.leagueSeason)
        editor.apply()
    }

    fun getCurrentLeague(context: Context): League? {
        try{
            val sharedPreferences = context.getSharedPreferences(prefKey, Context.MODE_PRIVATE)
            val leagueId = sharedPreferences.getInt("leagueId", 4708)
            val leagueShortcut = sharedPreferences.getString("leagueShortcut", null)
            val leagueSeason = sharedPreferences.getString("leagueSeason", null)

            if(leagueShortcut == null || leagueSeason == null)
                return null

            return League(leagueId, leagueShortcut, leagueSeason)
        }catch (e:Exception){
            return null
        }
    }

    fun removeCurrentLeague(context: Context){
        val sharedPreferences = context.getSharedPreferences(prefKey, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.remove("leagueId")
        editor.remove("leagueShortcut")
        editor.remove("leagueSeason")
        editor.apply()
    }
}