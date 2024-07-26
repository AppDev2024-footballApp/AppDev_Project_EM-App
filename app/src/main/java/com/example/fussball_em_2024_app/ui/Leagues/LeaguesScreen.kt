package com.example.fussball_em_2024_app.ui.Leagues

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.fussball_em_2024_app.LeagueDetail
import com.example.fussball_em_2024_app.LocalColors
import com.example.fussball_em_2024_app.R
import com.example.fussball_em_2024_app.model.League
import com.example.fussball_em_2024_app.ui.SimpleText
import com.example.fussball_em_2024_app.ui.TextAlignCenter
import com.example.fussball_em_2024_app.utils.StoreLeague
import com.example.fussball_em_2024_app.viewModels.LeaguesViewModel


@Composable
fun LeaguesScreen(
    navController: NavController,
    modifier: Modifier = Modifier
){
    val leaguesViewModel: LeaguesViewModel = viewModel()
    val viewState by leaguesViewModel.leaguesState

    val startLeague = StoreLeague().getCurrentLeague(LocalContext.current)
    if(startLeague != null){
        navController.navigate("${LeagueDetail.route}/${startLeague.leagueId}/${startLeague.leagueShortcut}/${startLeague.leagueSeason}")
    }

    Box(modifier = Modifier.fillMaxSize()){
        when{
            viewState.loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            viewState.error != null -> {
                SimpleText("ERROR OCCURRED")
            }
            else -> {
                Column (
                    modifier = modifier
                        .fillMaxSize()
                ){
                    if(viewState.list.isNotEmpty()){
                        SearchAndSortSection(leaguesViewModel)
                        LeaguesList(leagues = viewState.filteredList, navController = navController)
                    }else{
                        SimpleText("No such items found.")
                    }
                }
            }
        }
    }

}

@Composable
fun SearchAndSortSection(leaguesViewModel: LeaguesViewModel){
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var sortByName by remember { mutableStateOf(true) }
    var ascendingName by remember { mutableStateOf(true) }
    var ascendingSeason by remember { mutableStateOf(true) }
    var filterBySuggested by remember { mutableStateOf(false)}

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                leaguesViewModel.searchLeagues(it.text)
            },
            label = { Text("Search by League Name"/*, color = LocalTextColor.current // see below why this is comment*/) },
            modifier = Modifier
                .fillMaxWidth()
                // textField does not matter what in background is defined, therefor LightMode format is always for the TextField
                //.background(if(LightDarkModeHelper.isDarkMode(LocalTextColor.current)) Color.Gray else Color.White)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {

            Button(onClick = { sortByName = true; ascendingName = !ascendingName; leaguesViewModel.sortLeagues(byName = true, ascending = ascendingName) }) {
                SimpleText("Sort by Name")
                if(ascendingName){
                    Icon(Icons.Rounded.KeyboardArrowDown, contentDescription = "Arrow Down", tint = LocalColors.current.textColor)
                }
                else{
                    Icon(Icons.Rounded.KeyboardArrowUp, contentDescription = "Arrow Up", tint = LocalColors.current.textColor)
                }

            }
            Button(onClick = { sortByName = false; ascendingSeason = !ascendingSeason; leaguesViewModel.sortLeagues(byName = false, ascending = ascendingSeason) }) {
                SimpleText("Sort by Season")
                if(ascendingSeason){
                    Icon(Icons.Rounded.KeyboardArrowDown, contentDescription = "Arrow Down", tint = LocalColors.current.textColor)
                }
                else{
                    Icon(Icons.Rounded.KeyboardArrowUp, contentDescription = "Arrow Up", tint = LocalColors.current.textColor)
                }
            }
            IconButton(onClick = {
                filterBySuggested = !filterBySuggested;
                leaguesViewModel.filterLeaguesBySuggested(filterBySuggested);
                if (!filterBySuggested){ // reset screen after 2nd click
                    ascendingName = true
                    ascendingSeason = true
                }
                }) {
                if(filterBySuggested){
                    Icon(imageVector = Icons.Rounded.Star, contentDescription = "suggested Leagues", tint = LocalColors.current.textColor)
                } else{
                    Icon(painterResource(id = R.drawable.baseline_star_outline_24), contentDescription = "suggested Leagues", tint = LocalColors.current.textColor)
                    /* //Icon(imageVector = Icons.Outlined.StarRate, contentDescription = "suggested Leagues", tint = LocalTextColor.current)
                    Icons.Outlined.StarRate has not an outlined star!, therefor either have to import large library androidx.compose.material:material-icons-extended
                    or as now creating a vector asset in drawable (https://stackoverflow.com/questions/74050270/compose-icons-outlined-star-isnt-outlined)
                    */
                }

            }
        }
    }
}

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