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

                    val allLeagues = response.filter { league: League ->
                        league.sport.sportId == 1
                                && (league.leagueShortcut == "em"
                                || league.leagueShortcut == "wwc"
                                || league.leagueShortcut.startsWith("bl")
                                || league.leagueShortcut.startsWith("fem")
                                || league.leagueShortcut.startsWith("wm"))
                                && league.leagueId != 4684
                                && !league.leagueName.lowercase().contains("test")}

                    _leaguesState.value = _leaguesState.value.copy(
                        loading = false,
                        list = allLeagues,
                        filteredList = allLeagues,
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

    fun searchLeagues(query: String){
        val filteredList = if (query.isEmpty()) {
            _leaguesState.value.list
        } else {
            _leaguesState.value.list.filter {
                it.leagueName.contains(query, ignoreCase = true)
            }
        }
        _leaguesState.value = _leaguesState.value.copy(filteredList = filteredList)
    }

    fun sortLeagues(byName: Boolean, ascending: Boolean) {
        val sortedList = if (byName) {
            if(ascending){
                _leaguesState.value.filteredList.sortedBy { it.leagueName }
            }else{
                _leaguesState.value.filteredList.sortedByDescending { it.leagueName }
            }
        } else {
            if(ascending){
                _leaguesState.value.filteredList.sortedBy { it.leagueSeason }
            }else{
                _leaguesState.value.filteredList.sortedByDescending { it.leagueSeason }
            }
        }
        _leaguesState.value = _leaguesState.value.copy(filteredList = sortedList)
    }


    data class LeaguesState(
        val loading: Boolean = true,
        val list: List<League> = emptyList(),
        val filteredList: List<League> = emptyList(),
        val error: String? = null
    )
}