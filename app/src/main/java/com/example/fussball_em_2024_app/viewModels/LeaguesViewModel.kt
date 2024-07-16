package com.example.fussball_em_2024_app.viewModels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fussball_em_2024_app.matchService
import com.example.fussball_em_2024_app.model.League
import kotlinx.coroutines.launch

class LeaguesViewModel : ViewModel() {
    private val _leaguesState = mutableStateOf(LeaguesState())
    val leaguesState: State<LeaguesState> = _leaguesState

    init{
        fetchLeagues()
    }

    private fun fetchLeagues(){
        viewModelScope.launch {
            try{
                val response = matchService.getAllLeagues()
                if(response.isNotEmpty()){

                    _leaguesState.value = _leaguesState.value.copy(
                        loading = false,
                        list = response.filter { league: League ->  league.sport.sportId == 1
                            && (league.leagueShortcut == "em"
                                || league.leagueShortcut == "wwc"
                                || league.leagueShortcut.startsWith("bl")
                                || league.leagueShortcut.startsWith("fem")
                                || league.leagueShortcut.startsWith("wm"))
                                && league.leagueId != 4684
                                && !league.leagueName.lowercase().contains("test")}
                            .sortedBy { league: League ->  league.leagueSeason}.reversed(),
                        error = null
                    )

                }

            }catch (e:Exception){
                _leaguesState.value = _leaguesState.value.copy(
                    loading = false,
                    error = "Error fetching Leagues: ${e.message}"
                )
                Log.d("FetchError", e.message.toString())
            }
        }
    }


    data class LeaguesState(
        val loading: Boolean = true,
        val list: List<League> = emptyList(),
        val error: String? = null
    )
}