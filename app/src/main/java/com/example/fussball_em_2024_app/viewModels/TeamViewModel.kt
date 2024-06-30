package com.example.fussball_em_2024_app.viewModels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fussball_em_2024_app.matchService
import com.example.fussball_em_2024_app.model.Team
import kotlinx.coroutines.launch

class TeamViewModel(): ViewModel() {

    private val _teamState= mutableStateOf(TeamState())
    val teamState: State<TeamState> = _teamState

    init {
        fetchTeams()
    }


    private fun fetchTeams(){
        viewModelScope.launch {
            try {
                val response= matchService.getTeams()
                if (response.isNotEmpty()) {
                    _teamState.value= _teamState.value.copy(
                        loading = false,
                        list= response,
                        error = null
                    )
                }
            }catch (e:Exception){
               _teamState.value= _teamState.value.copy(
                    loading = false,
                    error = "Error fetching Matches: ${e.message}"
                )
                Log.d("FetchError", e.message.toString())
            }
        }
    }

    data class TeamState(
        val loading: Boolean = true,
        val list: List<Team> = emptyList(),
        val error: String?=null

    )
}