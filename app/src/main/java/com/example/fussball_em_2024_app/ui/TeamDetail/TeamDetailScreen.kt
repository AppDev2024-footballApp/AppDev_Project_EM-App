package com.example.fussball_em_2024_app.ui.TeamDetail

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.fussball_em_2024_app.LocalLeague
import com.example.fussball_em_2024_app.LocalTextColor
import com.example.fussball_em_2024_app.ui.Main.LastMatchScreen
import com.example.fussball_em_2024_app.ui.Main.NextMatchScreen

import com.example.fussball_em_2024_app.MatchDetail
import com.example.fussball_em_2024_app.MatchItems

import com.example.fussball_em_2024_app.viewModels.TeamDetailViewModel
import com.example.fussball_em_2024_app.viewModels.TeamDetailViewModelFactory
import com.example.fussball_em_2024_app.model.Match
import com.example.fussball_em_2024_app.viewModels.LastMatchesViewModel
import com.example.fussball_em_2024_app.viewModels.LastMatchesFactory


@Composable
fun TeamDetailScreen(teamId: Int, navController: NavController, modifier: Modifier = Modifier) {
    val league = LocalLeague.current
    val teamDetailViewModel: TeamDetailViewModel = viewModel(
        factory = TeamDetailViewModelFactory(teamId, league.leagueId, league.leagueShortcut, league.leagueSeason)
    )
    val teamInfo by teamDetailViewModel.teamInfoState
    val nextMatch by teamDetailViewModel.nextMatchState
    val lastMatch by teamDetailViewModel.lastMatchState

    Box(modifier = modifier.fillMaxSize()) {
        when{
    val teamName = teamInfo.teamInfo?.teamName
    val league = "UEFA EURO 2024"
    val lastMatchesViewModel: LastMatchesViewModel? = teamName?.let {
        viewModel(factory = LastMatchesFactory(it, pastWeeks = 8))
    }

    val lastMatches = lastMatchesViewModel?.matchState


    // exclude the last match, and sort by date
    fun getUniqueAndSortedMatches(matches: List<Match>, lastMatch: Match?): List<Match> {
        val seen = mutableSetOf<String>()
        return matches
            .filter { match ->
                val team1Name = match.team1.teamName
                val team2Name = match.team2.teamName
                val shouldExclude = match.matchID == lastMatch?.matchID || match.leagueName != league
                Log.i("Filtering", "MatchID: ${match.matchID}, LastMatchID: ${lastMatch?.matchID}, Exclude: $shouldExclude, League: ${match.leagueName}")
                // Check for the league and last match exclusion
                if ( shouldExclude) {
                    false
                } else {
                    val uniqueId = listOf(team1Name, team2Name).sorted().joinToString("-")
                    if (seen.contains(uniqueId)) {
                        false
                    } else {
                        seen.add(uniqueId)
                        true
                    }
                }
            }
            .sortedByDescending { match -> match.matchDateTime }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            teamInfo.error != null -> {
                Text("ERROR OCCURRED: ${teamInfo.error}")
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally) {

                    when{
                        teamInfo.teamInfo == null -> {
                            Text("ERROR OCCURRED")
                        }
                        else -> {
                            // Team name as a headline
                            Text(
                                text = teamInfo.teamInfo!!.teamName,
                                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 48.sp),
                                textAlign = TextAlign.Center,
                                color = LocalTextColor.current,
                                modifier = modifier.padding(bottom = 10.dp)
                            )
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        // Team name as a headline
                        teamInfo.teamInfo?.teamName?.let {
                            Text(
                                text = it,
                                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 48.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(bottom = 10.dp)
                            )
                        }

                            Image(
                                painter = rememberAsyncImagePainter(model = teamInfo.teamInfo?.teamIconUrl),
                                contentDescription = "Logo von ${teamInfo.teamInfo?.teamName}",
                                modifier = modifier
                                    .size(60.dp)
                                    .aspectRatio(1f)
                                    .clip(CircleShape),  // Macht das Bild kreisförmig
                                contentScale = ContentScale.Crop
                            )

                            // Team details
                            Text(
                                text = "Points: ${teamInfo.teamInfo?.points}",
                                style = TextStyle(fontSize = 30.sp),
                                color = LocalTextColor.current,
                                modifier = modifier.padding(bottom = 16.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Team Points & Goal Points
                            TeamPoints(teamInfo = teamInfo.teamInfo!!, modifier)
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }

                        nextMatch.match?.let { match ->
                            NextMatchScreen(match = match, navController)
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        lastMatch.match?.let { match ->
                            LastMatchScreen(match = match, navController)
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Display past matches
                        when {
                            lastMatches == null -> {
                                Text("Loading team info...", modifier = Modifier.padding(16.dp))
                            }
                            lastMatches.value.loading -> {
                                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                            }
                            lastMatches.value.error != null -> {
                                Text("ERROR OCCURRED: ${lastMatches.value.error}")
                            }
                            lastMatches.value.list.isNotEmpty() -> {
                                Text(
                                    text = "Last Games",
                                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp),
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }
                            else -> {
                                Text("No past matches found.", modifier = Modifier.padding(16.dp))
                            }
                        }
                    }

                    val filteredMatches = getUniqueAndSortedMatches(lastMatches?.value?.list ?: emptyList(), lastMatch.match)
                    filteredMatches.forEach { match ->
                        Log.i("FinalMatches", "MatchID: ${match.matchID}, League: ${match.leagueName}")
                    }

                    items(filteredMatches) { match ->
                        MatchItem(match = match, navController = navController)
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    item {
                        // Back button
                        Button(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                        ) {
                            Text("← Back")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MatchItem(match: Match, navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White, shape = RoundedCornerShape(4.dp))
            .clickable {
                navController.navigate("${MatchDetail.route}/${match.matchID}")
            }
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            MatchItems(match = match)
        }
    }
}
