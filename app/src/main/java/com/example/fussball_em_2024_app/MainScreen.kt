package com.example.fussball_em_2024_app

import MainViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fussball_em_2024_app.model.Match

@Composable
fun MatchScreen(modifier:Modifier=Modifier){
    val matchViewModel:MainViewModel= viewModel()
    val viewState by matchViewModel.matchState
    Box(modifier = Modifier.fillMaxSize()){
        when{

            viewState.error !=null ->{
                Text("ERROR OCCURRED")
            }
            else ->{
               CategoryScreen(matches = viewState.list)
            }
        }
    }
}

@Composable
fun CategoryScreen(matches: List<Match>){
    LazyVerticalGrid(GridCells.Fixed(1), modifier = Modifier.fillMaxSize()){
        items(matches){
                match ->
            MatchItems(match = match)
        }
    }
}

@Composable
fun MatchItems(match: Match) {
    Column(modifier = Modifier
        .padding(8.dp)
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Text(
            text= "Team1 ${match.team1.teamName}",
            style = TextStyle(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(top=4.dp)

        )

        Text(
            text= "Team2 ${match.team2.teamName}",
            style = TextStyle(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(top=4.dp)

        )

    }
}
