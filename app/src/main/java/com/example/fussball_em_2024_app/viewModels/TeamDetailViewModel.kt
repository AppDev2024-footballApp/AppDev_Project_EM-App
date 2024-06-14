package com.example.fussball_em_2024_app.viewModels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fussball_em_2024_app.matchService
import com.example.fussball_em_2024_app.model.Match
import com.example.fussball_em_2024_app.model.TeamInfo
import com.example.fussball_em_2024_app.viewModels.MatchViewModel.MatchState
import kotlinx.coroutines.launch

class TeamDetailViewModel() : ViewModel() {
    private val _teamInfoState = mutableStateOf(TeamInfoState())
    val teamInfoState: State<TeamInfoState> = _teamInfoState
    private val _nextMatchState= mutableStateOf(MatchState())
    val nextMatchState: State<MatchState> = _nextMatchState
    private val _lastMatchState= mutableStateOf(MatchState())
    val lastMatchState= _lastMatchState

    init {
        fetchTeam()
        fetchNextMatch()
        fetchLastMatch()
    }

    private fun fetchTeam(){
        viewModelScope.launch{
            try {
                val response= matchService.getTeamsDetails()
                if (response != null) {
                    for(team in response){
                        if(team.teamInfoId == 6169){
                            _teamInfoState.value= _teamInfoState.value.copy(
                                teamInfo = team,
                                loading = false,
                                error = null
                            )
                            break
                        }
                    }
                }

            }catch (e:Exception){
                _teamInfoState.value= _teamInfoState.value.copy(
                    loading = false,
                    error = "Error fetching Team Info: ${e.message}"
                )
                Log.d("FetchError", e.message.toString())
            }
        }
    }

    private fun fetchNextMatch(){
        viewModelScope.launch {
            try {

                val response= matchService.getNextMatchByTeam(6169)
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
        viewModelScope.launch {
            try {

                val response= matchService.getLastMatchByTeam(6169)
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

    data class TeamInfoState(
        val loading:Boolean= true,
        val teamInfo: TeamInfo?=null,
        val error:String?=null) {
    }



    data class MatchState(
        val loading:Boolean= true,
        val match: Match?=null,
        val error:String?=null) {
    }


}