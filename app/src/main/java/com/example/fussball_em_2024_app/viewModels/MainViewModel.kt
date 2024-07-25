package com.example
.fussball_em_2024_app
.viewModels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fussball_em_2024_app.matchService
import com.example.fussball_em_2024_app.model.Match
import kotlinx.coroutines.launch

class MainViewModelFactory(private val leagueShortcut: String, private val leagueSeason: String) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(leagueShortcut, leagueSeason) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class MainViewModel(private val leagueShortcut: String, private val leagueSeason: String) : ViewModel() {
    private val _matchState= mutableStateOf(MatchState())
    val matchState: State<MatchState> = _matchState

    init{
        fetchMatches()
    }

    private fun fetchMatches(){
        viewModelScope.launch {
            try {
                val response= matchService.getLatestMatch(leagueShortcut, leagueSeason)
                if (response.isNotEmpty()) {
                    _matchState.value= _matchState.value.copy(
                        list= response,
                        loading = false,
                        error = null
                    )
                }
            }catch (e:Exception){
                _matchState.value= _matchState.value.copy(
                    loading = false,
                    error = "Error fetching Matches: ${e.message}"
                )
                Log.d("FetchError", e.message.toString())
            }
        }
    }

    data class MatchState(
        val loading:Boolean= true,
        val list: List<Match> = emptyList(),
        val error:String?=null)

}
