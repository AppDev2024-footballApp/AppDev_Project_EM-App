package com.example.fussball_em_2024_app.ui.MatchDetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.fussball_em_2024_app.R
import com.example.fussball_em_2024_app.model.Goal
import com.example.fussball_em_2024_app.model.Match
import com.example.fussball_em_2024_app.ui.SimpleText
import com.example.fussball_em_2024_app.ui.TextAlignCenter


@Composable
fun GoalsScreen(match: Match){
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            SimpleText(match.team1.teamName)
        }

        Column(horizontalAlignment = Alignment.End) {
            SimpleText(match.team2.teamName)
        }
    }

    HorizontalDivider(color = Color.Black, thickness = 3.dp)

    Spacer(modifier = Modifier.height(20.dp))

    // Goal List
    if(match.matchIsFinished && match.goals == null || match.goals!!.isEmpty())
        SimpleText(text = "No goal data available")
    else{
        GoalList(goals = match.goals!!)
    }
}

@Composable
fun GoalList(goals: List<Goal>){
    var team1GoalNumber = 0
    var team2GoalNumber = 0

    goals.forEach{ goal ->
        if (goal.scoreTeam1!! > team1GoalNumber){
            team1GoalNumber++
            GoalItem(goal, true)
        }
        else if (goal.scoreTeam2!! > team2GoalNumber){
            team2GoalNumber++
            GoalItem(goal, false)
        }
    }
}


@Composable
fun GoalItem(goal: Goal, isFirstTeam: Boolean) {
    Column(modifier = Modifier.fillMaxSize(),horizontalAlignment = if (isFirstTeam) Alignment.Start else Alignment.End){
        Column(modifier = Modifier.fillMaxWidth(0.5f), horizontalAlignment = if (isFirstTeam) Alignment.End else Alignment.Start) {
            Row {
                if (isFirstTeam){
                    SimpleText(
                        text = if(goal.matchMinute != null) (goal.matchMinute.toString() + "' ") else "no data "
                    )
                    Image(
                        painter = painterResource(id = R.drawable.football),
                        contentDescription = "football"
                    )
                }else {
                    Image(
                        painter = painterResource(id = R.drawable.football),
                        contentDescription = "football"
                    )
                    SimpleText(text = if(goal.matchMinute != null) (" " + goal.matchMinute.toString() + "'") else " no data")
                }
            }
        }

        if(goal.comment != null){
            TextAlignCenter(
                text = "${goal.getGoalGetterName}\n" +
                        "${goal.comment}"
            )
        }
        else if(goal.isOwnGoal == true){
            TextAlignCenter(
                text = "${goal.getGoalGetterName}\n" +
                        "(OG)"
            )
        }
        else{
            TextAlignCenter(text = goal.getGoalGetterName)
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}