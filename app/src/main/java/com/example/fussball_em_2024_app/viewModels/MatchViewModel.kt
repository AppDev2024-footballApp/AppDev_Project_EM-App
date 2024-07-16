package com.example.fussball_em_2024_app.viewModels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.fussball_em_2024_app.data.AppDatabase
import com.example.fussball_em_2024_app.entity.FavouriteTeam
import com.example.fussball_em_2024_app.matchService
import com.example.fussball_em_2024_app.model.Match
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MatchViewModelFactory(private val context: Context, private val leagueId: Int, private val leagueShortcut: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MatchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MatchViewModel(context, leagueId, leagueShortcut) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class MatchViewModel(private val myContext: Context, private val leagueId: Int, private val leagueShortcut: String): ViewModel() {
    private val _nextMatchState= mutableStateOf(MatchState())
    val nextMatchState: State<MatchState> = _nextMatchState
    private val _lastMatchState= mutableStateOf(MatchState())
    val lastMatchState= _lastMatchState

    init {
        fetchNextMatch()
        fetchLastMatch()
    }

    private fun fetchNextMatch(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val favoriteTeams = getFavoriteTeams()
                var response: Match? = null

                for(favouriteTeam in favoriteTeams){
                    try{
                        val match = matchService.getNextMatchByTeam(leagueId, favouriteTeam.apiTeamId)
                        if(response == null || response.matchDateTime > match.matchDateTime)
                            response = match
                    }catch (e: Exception){
                        Log.d("FetchError", e.message.toString())
                    }
                }

                if (response == null) {
                    response = matchService.getNextMatch(leagueShortcut)
                }
                if (response != null) {
                    _nextMatchState.value= _nextMatchState.value.copy(
                        match= response,
                        loading = false,
                        error = null
                    )
                }

            }catch (e:Exception){
                _nextMatchState.value= _nextMatchState.value.copy(
                    loading = false,
                    error = "Error fetching Next Match: ${e.message}"
                )
                Log.d("FetchError", e.message.toString())
            }
        }
    }

    private fun fetchLastMatch(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val favoriteTeams = getFavoriteTeams()
                var response: Match? = null

                for(favouriteTeam in favoriteTeams){
                    try{
                        val match = matchService.getLastMatchByTeam(leagueId, favouriteTeam.apiTeamId)
                        if(response == null || response.matchDateTime < match.matchDateTime)
                            response = match
                    }catch (e: Exception){
                        Log.d("FetchError", e.message.toString())
                    }
                }

                if (response == null) {
                    response = matchService.getLastMatch(leagueShortcut)
                }
                if (response != null) {
                    _lastMatchState.value= _lastMatchState.value.copy(
                        match= response,
                        loading = false,
                        error = null
                    )
                }

            }catch (e:Exception){
                _lastMatchState.value= _lastMatchState.value.copy(
                    loading = false,
                    error = "Error fetching Last Match: ${e.message}"
                )
                Log.d("FetchError", e.message.toString())
            }
        }
    }

    private fun getFavoriteTeams(): List<FavouriteTeam> {
        val db = Room.databaseBuilder(
            context = myContext,
            AppDatabase::class.java, "FootballAppDB"
        ).build()
        return db.favouriteTeamDao().findByLeagueName("em24")
    }


    data class MatchState(
        val loading:Boolean= true,
        val match: Match?=null,
        val error:String?=null) {

    }
}
