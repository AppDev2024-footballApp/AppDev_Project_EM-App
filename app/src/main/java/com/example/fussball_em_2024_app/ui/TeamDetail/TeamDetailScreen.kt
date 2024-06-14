package com.example.fussball_em_2024_app.ui.TeamDetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fussball_em_2024_app.LastMatchScreen
import com.example.fussball_em_2024_app.NextMatchScreen
import com.example.fussball_em_2024_app.viewModels.TeamDetailViewModel

@Composable
fun TeamDetailScreen(teamId: Int, modifier: Modifier = Modifier) {
    val teamDetailViewModel: TeamDetailViewModel = viewModel()
    val teamInfo by teamDetailViewModel.teamInfoState
    val nextMatch by teamDetailViewModel.nextMatchState
    val lastMatch by teamDetailViewModel.lastMatchState

    Box(modifier = Modifier.fillMaxSize()) {
        when{
            teamInfo.error != null -> {
                Text("ERROR OCCURRED")
            }
            else -> {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Team name as a headline
                    teamInfo.teamInfo?.teamName?.let {
                        Text(
                            text = it,
                            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }

                    // Team details
                    Text(
                        text = "Points: ${teamInfo.teamInfo?.points}",
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Wins: ${teamInfo.teamInfo?.won}",
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Losses: ${teamInfo.teamInfo?.lost}",
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Draws: ${teamInfo.teamInfo?.draw}",
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Goals: ${teamInfo.teamInfo?.goals}",
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Opponent Goals: ${teamInfo.teamInfo?.opponentGoals}",
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Goal Diff: ${teamInfo.teamInfo?.goalDiff}",
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    nextMatch.match?.let { match ->
                        NextMatchScreen(match = match)
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    lastMatch.match?.let { match ->
                        LastMatchScreen(match = match)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}