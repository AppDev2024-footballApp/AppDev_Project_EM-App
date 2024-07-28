package com.example.fussball_em_2024_app.ui.Main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.fussball_em_2024_app.LocalLeague
import com.example.fussball_em_2024_app.TeamDetail
import com.example.fussball_em_2024_app.model.Team
import com.example.fussball_em_2024_app.ui.BasicButton
import com.example.fussball_em_2024_app.ui.SimpleText
import com.example.fussball_em_2024_app.ui.TeamFlagImage
import com.example.fussball_em_2024_app.ui.TextAlignCenter
import com.example.fussball_em_2024_app.utils.StoreLeague
import com.example.fussball_em_2024_app.viewModels.MatchViewModel
import com.example.fussball_em_2024_app.viewModels.MatchViewModelFactory
import com.example.fussball_em_2024_app.viewModels.TeamViewModel
import com.example.fussball_em_2024_app.viewModels.TeamViewModelFactory

@Composable
fun MatchScreen(navController: NavController, modifier: Modifier = Modifier) {
    val teamViewModel: TeamViewModel = viewModel(factory = TeamViewModelFactory(LocalLeague.current.leagueShortcut, LocalLeague.current.leagueSeason))
    val viewState by teamViewModel.teamState
    val matchViewModel: MatchViewModel = viewModel(factory = MatchViewModelFactory(LocalContext.current, LocalLeague.current.leagueId, LocalLeague.current.leagueShortcut))
    val nextViewState by matchViewModel.nextMatchState
    val lastViewState by matchViewModel.lastMatchState
    val context = LocalContext.current
    val league = LocalLeague.current

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            viewState.loading -> {
                // Zeigt den Ladekreis in der Mitte des Bildschirms an
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            viewState.error != null -> {
                SimpleText("ERROR OCCURRED")
                BasicButton("Other leagues", Modifier.padding(top = 16.dp)) {
                    StoreLeague().removeCurrentLeague(context)
                    navController.navigate("overview")
                }
            }
            else -> {
                // Layz Column in order to be scrollable
                LazyColumn (
                    modifier = modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                )
                {
                    // Show the next match first
                    nextViewState.match?.let { match ->
                        item{
                            NextMatchScreen(match = match, navController = navController)
                        }

                    }

                    // Show the last match
                    lastViewState.match?.let { match->
                        item{
                            LastMatchScreen(match = match, navController = navController)
                        }
                    }

                    item {
                        BasicButton("Other leagues", Modifier.padding(top = 8.dp)) {
                            StoreLeague().removeCurrentLeague(context)
                            navController.navigate("overview")
                        }
                    }

                    item {
                        FavouriteTeams(teams = viewState.list, onTeamClick = { selectedTeam ->
                            navController.navigate("${TeamDetail.route}/${league.leagueId}/${league.leagueShortcut}/${league.leagueSeason}/${selectedTeam.teamId}")
                        })
                    }

                    item{
                        Column {
                            SimpleText("All Teams")
                            if (viewState.list.isEmpty()) {
                                SimpleText("No Such items Found.")
                            }
                        }
                    }
                    if(viewState.list.isNotEmpty()){
                        items(viewState.list) { team ->
                            TeamItem(team = team) { selectedTeam ->
                                navController.navigate("${TeamDetail.route}/${league.leagueId}/${league.leagueShortcut}/${league.leagueSeason}/${selectedTeam.teamId}")
                            }
                        }
                    }
                }
            }
        }
    }
}




@Composable
fun TeamItem(team:Team, onTeamClick: (Team) -> Unit){
    Column(modifier = Modifier
        .padding(8.dp)
        .clickable { onTeamClick(team) }) {
        // Oberer Teil mit den Logos
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Circle image with team name for team1
            Row {
                TeamFlagImage(team = team)
                Column {
                    team.teamGroupName?.let {
                        TextAlignCenter(it )
                    }
                    SimpleText(text = team.teamName)
                }
            }
        }
    }

}