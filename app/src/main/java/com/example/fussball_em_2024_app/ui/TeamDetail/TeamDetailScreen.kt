package com.example.fussball_em_2024_app.ui.TeamDetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.fussball_em_2024_app.LocalColors
import com.example.fussball_em_2024_app.LocalLeague
import com.example.fussball_em_2024_app.ui.Main.LastMatchScreen
import com.example.fussball_em_2024_app.ui.Main.NextMatchScreen
import com.example.fussball_em_2024_app.ui.SimpleText
import com.example.fussball_em_2024_app.ui.TextAlignCenter
import com.example.fussball_em_2024_app.viewModels.TeamDetailViewModel
import com.example.fussball_em_2024_app.viewModels.TeamDetailViewModelFactory

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
            teamInfo.error != null -> {
                SimpleText("ERROR OCCURRED")
            }
            else -> {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally) {

                    when{
                        teamInfo.teamInfo == null -> {
                            SimpleText("ERROR OCCURRED")
                        }
                        else -> {
                            // Team name as a headline
                            TextAlignCenter(
                                text = teamInfo.teamInfo!!.teamName,
                                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 48.sp),
                                modifier = modifier.padding(bottom = 10.dp)
                            )

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
                            SimpleText(
                                text = "Points: ${teamInfo.teamInfo?.points}",
                                style = TextStyle(fontSize = 30.sp),
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

                    Spacer(modifier = Modifier.weight(1f)) // Fills remaining space

                    // Back button
                    Button(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.align(Alignment.Start)
                    ) {
                        SimpleText("← Back")
                    }
                }
            }
        }
    }
}