package com.example.fussball_em_2024_app.ui.TeamDetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fussball_em_2024_app.LocalTextColor
import com.example.fussball_em_2024_app.model.TeamInfo

@Composable
fun TeamPoints(teamInfo: TeamInfo, modifier: Modifier = Modifier){
    Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceAround
    ){
        Column {
            TeamInfoPointsText("Wins: ${teamInfo.won}", modifier)

            TeamInfoPointsText("Losses: ${teamInfo.lost}", modifier)

            TeamInfoPointsText("Draws: ${teamInfo.draw}", modifier)
        }

        Column {
            TeamInfoPointsText("Goals: ${teamInfo.goals}", modifier)

            TeamInfoPointsText("Opponent Goals: ${teamInfo.opponentGoals}", modifier)

            TeamInfoPointsText("Goal Diff: ${teamInfo.goalDiff}", modifier)
        }
    }
}

@Composable
fun TeamInfoPointsText(text: String, modifier: Modifier){
    Text(
        text = text,
        style = TextStyle(fontSize = 24.sp),
        color = LocalTextColor.current,
        modifier = modifier.padding(bottom = 8.dp)
    )
}