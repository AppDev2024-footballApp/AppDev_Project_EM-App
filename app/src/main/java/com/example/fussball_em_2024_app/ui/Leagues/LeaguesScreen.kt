package com.example.fussball_em_2024_app.ui.Leagues

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.fussball_em_2024_app.LeagueDetail
import com.example.fussball_em_2024_app.LocalTextColor
import com.example.fussball_em_2024_app.model.League
import com.example.fussball_em_2024_app.viewModels.LeaguesViewModel


@Composable
fun LeaguesScreen(
    navController: NavController,
    modifier: Modifier = Modifier
){
    val leaguesViewModel: LeaguesViewModel = viewModel()
    val viewState by leaguesViewModel.leaguesState

    Box(modifier = Modifier.fillMaxSize()){
        when{
            viewState.loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            viewState.error != null -> {
                Text("ERROR OCCURRED", color = LocalTextColor.current)
            }
            else -> {
                Column (
                    modifier = modifier
                        .fillMaxSize()
                ){
                    if(viewState.list.isNotEmpty()){
                        LeaguesList(leagues = viewState.list, navController = navController)
                    }else{
                        Text("No such items found.")
                    }
                }
            }
        }
    }

}

@Composable
fun LeaguesList(leagues: List<League>, navController: NavController){
    Column {
        Text(
            text="All Leagues", color = LocalTextColor.current
        )
    }

    LazyColumn(
        contentPadding = PaddingValues(all = 8.dp), // Füge Abstand der ganzen Liste hinzu
        modifier = Modifier.fillMaxSize()
    ) {
        items(leagues) { league ->
            LeagueItem(league = league, onLeagueClick = { selectedLeague ->
                navController.navigate("${LeagueDetail.route}/${league.leagueShortcut}/${league.leagueSeason}")
            })
        }
    }
}

@Composable
fun LeagueItem(league: League, onLeagueClick: (League) -> Unit){
    Column(modifier = Modifier
        .padding(8.dp)
        .clickable { onLeagueClick(league) }) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text= league.leagueName,
                    textAlign = TextAlign.Center,
                    color = LocalTextColor.current
                )
                Text(
                    text = league.leagueSeason,
                    textAlign = TextAlign.Center,
                    color = LocalTextColor.current
                )
            }

        }
    }
}