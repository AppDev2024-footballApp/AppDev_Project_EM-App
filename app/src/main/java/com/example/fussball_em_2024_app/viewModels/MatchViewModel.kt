package com.example.fussball_em_2024_app.viewModels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fussball_em_2024_app.matchService
import com.example.fussball_em_2024_app.model.Match
import kotlinx.coroutines.launch

class MatchViewModel: ViewModel() {
    private val _nextMatchState= mutableStateOf(MatchState())
    val nextMatchState: State<MatchState> = _nextMatchState
    private val _lastMatchState= mutableStateOf(MatchState())
    val lastMatchState= _lastMatchState

    init {
        fetchNextMatch()
        fetchLastMatch()
    }

    private fun fetchNextMatch(){
        viewModelScope.launch {
            try {

                val response= matchService.getNextMatch()
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

                val response= matchService.getLastMatch()
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


    data class MatchState(
        val loading:Boolean= true,
        val match: Match?=null,
        val error:String?=null) {

    }
}
