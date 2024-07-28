package com.example.fussball_em_2024_app.ui.Main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fussball_em_2024_app.model.Match
import com.example.fussball_em_2024_app.ui.TeamFlagImage
import com.example.fussball_em_2024_app.ui.TextAlignCenter
import com.example.fussball_em_2024_app.utils.DateFormater.formatDate

@Composable
fun MatchItem(match: Match) {
    Column(modifier = Modifier.padding(8.dp)) {
        // Oberer Teil mit den Logos
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Circle image with team name for team1
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                TeamFlagImage(team = match.team1)
                TextAlignCenter( text = match.team1.teamName.replace(" ", "\n")) // replace because of too long teamNames
            }

            // Datum des Spiels in der Mitte
            Column(Modifier.align(Alignment.CenterVertically)) {
                if(match.matchIsFinished){
                    val team1Score = match.goals?.lastOrNull()?.scoreTeam1 ?: 0
                    val team2Score = match.goals?.lastOrNull()?.scoreTeam2 ?: 0
                    TextAlignCenter(text = formatDate(match.matchDateTime) + "\n$team1Score - $team2Score")
                }else{
                    TextAlignCenter(
                        text = formatDate(match.matchDateTime),  // Datum formatieren nach Bedarf
                        //modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }

            // Circle image with team name for team2
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                TeamFlagImage(team = match.team2)
                TextAlignCenter(text = match.team2.teamName.replace(" ", "\n")) // replace because of too long teamNames
            }
        }
    }
}