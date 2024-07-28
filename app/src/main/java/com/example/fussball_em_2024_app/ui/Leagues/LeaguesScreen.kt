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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.fussball_em_2024_app.LeagueDetail
import com.example.fussball_em_2024_app.LocalColors
import com.example.fussball_em_2024_app.R
import com.example.fussball_em_2024_app.model.League
import com.example.fussball_em_2024_app.utils.StoreLeague
import com.example.fussball_em_2024_app.viewModels.LeaguesViewModel
import com.example.testjetpackcompose.ui.theme.buttonsColor
import com.example.testjetpackcompose.ui.theme.lightBeige
import com.example.testjetpackcompose.ui.theme.lightGreen


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
                Text("ERROR OCCURRED", color = LocalColors.current.textColor)
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
                        Text("No such items found.", color = LocalColors.current.textColor)
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
            shape = RoundedCornerShape(18.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "Search Icon",
                    tint = LocalColors.current.textColor
                )
            },
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

            Button(
                onClick = { sortByName = true; ascendingName = !ascendingName; leaguesViewModel.sortLeagues(byName = true, ascending = ascendingName) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonsColor, // Farbe des Buttons
                    contentColor = White,)
            ) {
                Text("Sort by Name", color = White)
                if(ascendingName){
                    Icon(Icons.Rounded.KeyboardArrowDown, contentDescription = "Arrow Down", tint = White)
                }
                else{
                    Icon(Icons.Rounded.KeyboardArrowUp, contentDescription = "Arrow Up", tint = White)
                }

            }
            Button(onClick = { sortByName = false; ascendingSeason = !ascendingSeason; leaguesViewModel.sortLeagues(byName = false, ascending = ascendingSeason) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buttonsColor, // Farbe des Buttons
                        contentColor = White,)
            ) {
                Text("Sort by Season", color = White)
                if(ascendingSeason){
                    Icon(Icons.Rounded.KeyboardArrowDown, contentDescription = "Arrow Down", tint = White)
                }
                else{
                    Icon(Icons.Rounded.KeyboardArrowUp, contentDescription = "Arrow Up", tint = White)
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
        Text(
            text="All Leagues", color = LocalColors.current.textColor
        )
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
                Text(
                    text= league.leagueName,
                    textAlign = TextAlign.Center,
                    color = LocalColors.current.textColor
                )
                Text(
                    text = league.leagueSeason,
                    textAlign = TextAlign.Center,
                    color = LocalColors.current.textColor
                )
            }
            if(league.isSuggested){
                Icon(imageVector = Icons.Rounded.Star, contentDescription = "suggested league", tint = LocalColors.current.textColor)
            }
        }
    }
}