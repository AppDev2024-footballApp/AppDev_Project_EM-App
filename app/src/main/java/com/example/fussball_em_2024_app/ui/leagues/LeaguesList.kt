package com.example.fussball_em_2024_app.ui.leagues

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fussball_em_2024_app.LeagueDetail
import com.example.fussball_em_2024_app.LocalColors
import com.example.fussball_em_2024_app.model.League
import com.example.fussball_em_2024_app.ui.SimpleText
import com.example.fussball_em_2024_app.ui.TextAlignCenter
import com.example.fussball_em_2024_app.utils.StoreLeague

@Composable
fun LeaguesList(leagues: List<League>, navController: NavController){
    Column {
        SimpleText(text="All Leagues")
    }

    val currentContext = LocalContext.current

    LazyColumn(
        contentPadding = PaddingValues(all = 8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(leagues) { league ->
            LeagueItem(league = league, onLeagueClick = { selectedLeague ->
                StoreLeague().saveCurrentLeague(selectedLeague, currentContext)
                navController.navigate("${LeagueDetail.route}/${league.leagueId}/${league.leagueShortcut}/${league.leagueSeason}")
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
                TextAlignCenter(text= league.leagueName)
                TextAlignCenter(text = league.leagueSeason)
            }
            if(league.isSuggested){
                Icon(imageVector = Icons.Rounded.Star, contentDescription = "suggested league", tint = LocalColors.current.textColor)
            }
        }
    }
}