package com.example.fussball_em_2024_app.viewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fussball_em_2024_app.matchService
import com.example.fussball_em_2024_app.model.Match
import kotlinx.coroutines.launch

class MatchDetailViewModelFactory(private val matchId: Int) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MatchDetailViewModel::class.java)) {
            return MatchDetailViewModel(matchId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class MatchDetailViewModel(matchId: Int) : ViewModel() {
    private val _matchState= mutableStateOf(SingleMatchState())
    val matchState: State<SingleMatchState> = _matchState

    init{
        fetchMatches(matchId)
    }


    private fun fetchMatches(matchId: Int){
        viewModelScope.launch {
            try {
                val response= matchService.getMatch(matchId)
                if (response != null) {
                    _matchState.value= _matchState.value.copy(
                        match = response,
                        loading = false,
                        error = null
                    )
                }
            }catch (e:Exception){
                _matchState.value= _matchState.value.copy(
                    loading = false,
                    error = "Error fetching Match: ${e.message}"
                )
                Log.d("FetchError", e.message.toString())
            }
        }
    }







    data class SingleMatchState(
        val loading:Boolean= true,
        val match: Match? = null,
        val error:String?=null)
}