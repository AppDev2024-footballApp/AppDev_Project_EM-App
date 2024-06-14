package com.example.fussball_em_2024_app

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.rememberAsyncImagePainter
import com.example.fussball_em_2024_app.model.Match
import com.example.fussball_em_2024_app.model.Team
import com.example.fussball_em_2024_app.ui.TeamDetail.TeamDetailScreen
import com.example.fussball_em_2024_app.viewModels.MatchViewModel
import com.example.fussball_em_2024_app.viewModels.TeamDetailViewModel
import com.example.fussball_em_2024_app.viewModels.TeamViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale



@Composable
fun MatchScreen(modifier: Modifier = Modifier) {
    val teamViewModel: TeamViewModel = viewModel()
    val viewState by teamViewModel.teamState
    val nextMatchViewModel: MatchViewModel = viewModel()
    val nextViewState by nextMatchViewModel.nextMatchState
    val lastMatchViewModel: MatchViewModel= viewModel()
    val lastViewState by lastMatchViewModel.lastMatchState

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            viewState.error != null -> {
                Text("ERROR OCCURRED")
            }
            else -> {
                Column {
                    // Zeige zuerst den nächsten Match an
                    nextViewState.match?.let { match ->
                        NextMatchScreen(match = match)
                        Spacer(modifier = Modifier.height(16.dp))

                    }


                    lastViewState.match?.let { match ->
                        LastMatchScreen(match = match)
                        Spacer(modifier = Modifier.height(16.dp))
                    }


                    // Zeige dann die Liste der CategoryScreen Matches
                    if (viewState.list.isNotEmpty()) {
                        TeamScreen(teams = viewState.list, onTeamClick = {})
                    } else {
                        Text("Keine weiteren Spiele gefunden.")
                    }
                }
            }
        }
    }
}

@Composable
fun TeamScreen(teams: List<Team>, onTeamClick: (Team) -> Unit) {
    // Eine LazyColumn ist bereits scrollbar
    LazyColumn(
        contentPadding = PaddingValues(all = 8.dp), // Füge Abstand der ganzen Liste hinzu
        modifier = Modifier.fillMaxSize()
    ) {
        items(teams) { team ->
            TeamItem(team = team, onTeamClick = onTeamClick)
        }
    }
}

@Composable
fun NextMatchScreen(match: Match) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Next Game:",
            style = TextStyle(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        MatchItems(match = match)
    }
}

@Composable
fun LastMatchScreen(match: Match) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Last Game",
            style = TextStyle(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        MatchItems(match = match)
    }
}

@Composable
fun MatchItems(match: Match) {
    Column(modifier = Modifier.padding(8.dp)) {
        // Oberer Teil mit den Logos
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Circle image with team name for team1
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = rememberAsyncImagePainter(model = match.team1.teamIconUrl),
                    contentDescription = "Logo von ${match.team1.teamName}",
                    modifier = Modifier
                        .size(60.dp)
                        .aspectRatio(1f)
                        .clip(CircleShape),  // Macht das Bild kreisförmig
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = match.team1.teamName,
                    textAlign = TextAlign.Center
                )
            }

            // Datum des Spiels in der Mitte
            Text(
                text = formatDate(match.matchDateTime),  // Datum formatieren nach Bedarf
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            // Circle image with team name for team2
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = rememberAsyncImagePainter(model = match.team2.teamIconUrl),
                    contentDescription = "Logo von ${match.team2.teamName}",
                    modifier = Modifier
                        .size(60.dp)
                        .aspectRatio(1f)
                        .clip(CircleShape),  // Macht das Bild kreisförmig
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = match.team2.teamName,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


// Funktion zum Konvertieren des Datums in String
@Composable
fun formatDate(date: Date?): String {
    return if(date != null) {
        SimpleDateFormat("dd.MM.yy \n'um' HH:mm 'Uhr'", Locale.GERMANY).format(date)
    } else {
        "Datum unbekannt"
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
            Row() {
                Image(
                    painter = rememberAsyncImagePainter(model = team.teamIconUrl),
                    contentDescription = "Logo von ${team.teamName}",
                    modifier = Modifier
                        .size(60.dp)
                        .aspectRatio(1f)
                        .clip(CircleShape),  // Macht das Bild kreisförmig
                    contentScale = ContentScale.Crop
                )
                Column {
                    team.teamGroupName?.let {
                        Text(
                            text= it,
                            textAlign = TextAlign.Center

                            )
                    }
                    Text(
                        text = team.teamName,
                        textAlign = TextAlign.Center

                    )
                }

            }

            // Datum des Spiels in der Mitte

        }
    }

}




