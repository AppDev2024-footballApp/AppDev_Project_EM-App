package com.example.fussball_em_2024_app.viewModels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fussball_em_2024_app.matchService
import com.example.fussball_em_2024_app.model.Match
import com.example.fussball_em_2024_app.model.TeamInfo
import kotlinx.coroutines.launch

class TeamDetailViewModelFactory(private val teamId: Int, private val leagueId: Int, private val leagueShortcut: String, private val leagueSeason: String) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TeamDetailViewModel::class.java)) {
            return TeamDetailViewModel(teamId, leagueId, leagueShortcut, leagueSeason) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class TeamDetailViewModel(teamId: Int, private val leagueId: Int, private val leagueShortcut: String, private val leagueSeason: String) : ViewModel() {
    private val _teamInfoState = mutableStateOf(TeamInfoState())
    val teamInfoState: State<TeamInfoState> = _teamInfoState
    private val _nextMatchState= mutableStateOf(MatchState())
    val nextMatchState: State<MatchState> = _nextMatchState
    private val _lastMatchState= mutableStateOf(MatchState())
    val lastMatchState= _lastMatchState

    init {
        fetchTeam(teamId)
        fetchNextMatch(teamId)
        fetchLastMatch(teamId)
    }

    private fun fetchTeam(teamId: Int){
        viewModelScope.launch{
            try {
                val response= matchService.getTeamsDetails(leagueShortcut, leagueSeason)
                if (response.isNotEmpty()) {
                    for(team in response){
                        if(team.teamInfoId == teamId){
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

    private fun fetchNextMatch(teamId: Int){
        viewModelScope.launch {
            try {

                val response= matchService.getNextMatchByTeam(leagueId, teamId)
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

    private fun fetchLastMatch(teamId: Int){
        viewModelScope.launch {
            try {

                val response= matchService.getLastMatchByTeam(leagueId, teamId)
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