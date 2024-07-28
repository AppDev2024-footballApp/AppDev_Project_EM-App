package com.example.fussball_em_2024_app.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.fussball_em_2024_app.data.AppDatabase
import com.example.fussball_em_2024_app.entity.FavouriteTeam
import com.example.fussball_em_2024_app.model.Team
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavouriteTeamsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavouriteTeamsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavouriteTeamsViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class FavouriteTeamsViewModel(context: Context) : ViewModel() {
    private val db = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java, "FootballAppDB"
    ).build()

    private val _favouriteTeams = MutableStateFlow<List<FavouriteTeam>>(emptyList())
    val favouriteTeams: StateFlow<List<FavouriteTeam>> = _favouriteTeams

    fun loadFavouriteTeams(leagueName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val favTeams = db.favouriteTeamDao().findByLeagueName(leagueName)
            _favouriteTeams.value = favTeams
        }
    }

    fun addFavouriteTeam(team: Team, leagueName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val teamAlreadyAdded = db.favouriteTeamDao().findByLeagueAndTeamName(league = leagueName, teamName = team.teamName)
            if(teamAlreadyAdded == null){
                db.favouriteTeamDao().insert(FavouriteTeam(apiTeamId = team.teamId, leagueName = leagueName, teamName = team.teamName))
                loadFavouriteTeams(leagueName)
            }
        }
    }

    fun removeFavouriteTeam(team: FavouriteTeam, leagueName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            db.favouriteTeamDao().delete(team)
            loadFavouriteTeams(leagueName)
        }
    }
}