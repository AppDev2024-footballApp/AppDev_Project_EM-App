package com.example.fussball_em_2024_app.ui.teamDetail

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.fussball_em_2024_app.LocalLeague
import com.example.fussball_em_2024_app.MatchDetail
import com.example.fussball_em_2024_app.MatchItems
import com.example.fussball_em_2024_app.model.Match
import com.example.fussball_em_2024_app.ui.Main.LastMatchScreen
import com.example.fussball_em_2024_app.ui.Main.NextMatchScreen
import com.example.fussball_em_2024_app.viewModels.LastMatchesFactory
import com.example.fussball_em_2024_app.viewModels.LastMatchesViewModel
import com.example.fussball_em_2024_app.ui.BasicButton
import com.example.fussball_em_2024_app.ui.main.LastMatchScreen
import com.example.fussball_em_2024_app.ui.main.NextMatchScreen
import com.example.fussball_em_2024_app.ui.SimpleText
import com.example.fussball_em_2024_app.ui.TeamFlagImage
import com.example.fussball_em_2024_app.ui.TextAlignCenter
import com.example.fussball_em_2024_app.viewModels.TeamDetailViewModel
import com.example.fussball_em_2024_app.viewModels.TeamDetailViewModelFactory
import com.example.testjetpackcompose.ui.theme.buttonsColor

@Composable
fun TeamDetailScreen(teamId: Int, navController: NavController, modifier: Modifier = Modifier) {
    val league = LocalLeague.current
    val teamDetailViewModel: TeamDetailViewModel = viewModel(
        factory = TeamDetailViewModelFactory(teamId, league.leagueId, league.leagueShortcut, league.leagueSeason)
    )
    val teamInfo by teamDetailViewModel.teamInfoState
    val nextMatch by teamDetailViewModel.nextMatchState
    val lastMatch by teamDetailViewModel.lastMatchState

    val teamName = teamInfo.teamInfo?.teamName
    val lastMatchesViewModel: LastMatchesViewModel? = teamName?.let {
        viewModel(factory = LastMatchesFactory(it, pastWeeks = 260)) // All Matches from last 5 Years
    }

    val lastMatches = lastMatchesViewModel?.matchState
    Log.i("LeagueInfo", "Current League Name: ${league.leagueShortcut}")

    // Helper function to filter unique matches,
    // exclude the last match, and sort by date
    fun getUniqueAndSortedMatches(matches: List<Match>, lastMatch: Match?): List<Match> {
        val seen = mutableSetOf<String>()
        return matches
            .filter { match ->
                val team1Name = match.team1.teamName
                val team2Name = match.team2.teamName
                val shouldExclude = match.matchID == lastMatch?.matchID || match.leagueShortcut!= league.leagueShortcut
                Log.i("Filtering", "MatchID: ${match.matchID}, Exclude: $shouldExclude, MatchLeagueShortcut: ${match.leagueShortcut} LeagueShortcut: ${league.leagueShortcut}")

                if (shouldExclude) {
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

    Box(modifier = modifier.fillMaxSize()) {
        when {
            teamInfo.error != null -> {
                SimpleText("ERROR OCCURRED: ${teamInfo.error}")
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        teamInfo.teamInfo?.teamName?.let {
                            TextAlignCenter(
                                text = it,
                                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 48.sp),
                                modifier = Modifier.padding(bottom = 10.dp)
                            )
                        }

                            if(teamInfo.teamInfo != null){
                                TeamFlagImage(teamInfo.teamInfo!!)
                            }

                        // Team details
                        SimpleText(
                            text = "Points: ${teamInfo.teamInfo?.points}",
                            style = TextStyle(fontSize = 30.sp),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Team Points & Goal Points
                        teamInfo.teamInfo?.let {
                            TeamPoints(teamInfo = it, modifier)
                            Spacer(modifier = Modifier.height(24.dp))
                        }

                        nextMatch.match?.let { match ->
                            NextMatchScreen(match = match, navController)
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        lastMatch.match?.let { match ->
                            LastMatchScreen(match = match, navController)
                            Spacer(modifier = Modifier.height(16.dp))
                        }



                        // Display past matches
                        when {
                            lastMatches == null -> {
                                Text("Loading team info...",
                                    modifier = Modifier.padding(16.dp),
                                    color= LocalColors.current.textColor)
                            }
                            lastMatches.value.loading -> {
                                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                            }
                            lastMatches.value.error != null -> {
                                Text("ERROR OCCURRED: ${lastMatches.value.error}",
                                    color= LocalColors.current.textColor)
                            }
                            lastMatches.value.list.isNotEmpty() -> {
                                Text(
                                    text = "Last Games",
                                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp),
                                    modifier = Modifier.padding(bottom = 8.dp),
                                    color= LocalColors.current.textColor
                                )
                            }
                            else -> {
                                Text("No past matches found.",
                                    modifier = Modifier.padding(16.dp),
                                    color= LocalColors.current.textColor)
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
                            colors = ButtonDefaults.buttonColors(
                                containerColor = buttonsColor, // Farbe des Buttons
                                contentColor = Color.White,
                            ),
                            modifier = Modifier.align(Alignment.Center)
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
            .padding(16.dp)
            .background(
                color = LocalColors.current.secondaryBackgroundColor,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable {
                navController.navigate("${MatchDetail.route}/${match.matchID}")
            }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            MatchItems(match = match)
        }
    }
}
