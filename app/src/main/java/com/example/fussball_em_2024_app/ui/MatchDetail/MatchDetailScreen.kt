package com.example.fussball_em_2024_app.ui.MatchDetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.fussball_em_2024_app.LocalTextColor
import com.example.fussball_em_2024_app.R
import com.example.fussball_em_2024_app.model.Goal
import com.example.fussball_em_2024_app.utils.DateFormater
import com.example.fussball_em_2024_app.viewModels.MatchDetailViewModel
import com.example.fussball_em_2024_app.viewModels.MatchDetailViewModelFactory

@Composable
fun MatchDetailScreen(
    matchId: Int,
    navController: NavController,
    modifier: Modifier = Modifier
) {

    val matchDetailViewModel: MatchDetailViewModel = viewModel(
        factory = MatchDetailViewModelFactory(matchId)
    )
    val matchInfo by matchDetailViewModel.matchState

    val scrollState = rememberScrollState()

    Box(modifier = modifier.fillMaxSize()) {
        when {
            matchInfo.error != null || matchInfo.match == null -> {
                Text("ERROR OCCURRED", color = LocalTextColor.current)
            }

            else -> {
                val match = matchInfo.match

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Game: ${match?.team1?.shortName} vs. ${match?.team2?.shortName}",
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp),
                        textAlign = TextAlign.Center,
                        color = LocalTextColor.current,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Column(horizontalAlignment = Alignment.Start) {
                            Image(
                                painter = rememberAsyncImagePainter(model = match?.team1?.teamIconUrl),
                                contentDescription = "Logo von ${match?.team1?.teamName}",
                                modifier = Modifier
                                    .size(60.dp)
                                    .aspectRatio(1f)
                                    .clip(CircleShape),  // Macht das Bild kreisförmig
                                contentScale = ContentScale.Crop
                            )
                        }

                        Column {
                            Text(
                                text = (if(match?.group?.groupName?.length!! > 1) match.group.groupName else "Group ${match.group.groupName}"),
                                style = TextStyle(fontSize = 18.sp),
                                textAlign = TextAlign.Center,
                                color = LocalTextColor.current,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Image(
                                painter = rememberAsyncImagePainter(model = match?.team2?.teamIconUrl),
                                contentDescription = "Logo von ${match?.team2?.teamName}",
                                modifier = Modifier
                                    .size(60.dp)
                                    .aspectRatio(1f)
                                    .clip(CircleShape),  // Macht das Bild kreisförmig
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    if (match?.matchIsFinished == true) {
                        TextDetailMatchInformation(text = "Finished\n" + "Started: ${DateFormater.formatDate(match.matchDateTime)}")
                    }
                    else if(DateFormater.isDateAfterNow(match!!.matchDateTimeUTC)){
                        TextDetailMatchInformation(text = "Ongoing")
                    }
                    else{
                        TextDetailMatchInformation(text = "Not Started\n" + "Starting at: ${DateFormater.formatDate(match.matchDateTime)}")
                    }

                    TextDetailMatchInformation(text = "Stadion: ${match.location?.locationStadium} (${match.location?.locationCity})")

                    if (match.numberOfViewers != null){
                        TextDetailMatchInformation(text = "Number of Viewers: ${match.numberOfViewers}")
                    }

                    // goals
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.Start) {
                            Text(match.team1.teamName, color = LocalTextColor.current)
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(match.team2.teamName, color = LocalTextColor.current)
                        }
                    }

                    HorizontalDivider(color = Color.Black, thickness = 3.dp)

                    Spacer(modifier = Modifier.height(16.dp))

                    var team1GoalNumber = 0
                    var team2GoalNumber = 0

                    match.goals!!.forEach{ goal ->
                        if (goal.scoreTeam1!! > team1GoalNumber){
                            team1GoalNumber++
                            GoalItem(goal, true)
                        }
                        else if (goal.scoreTeam2!! > team2GoalNumber){
                            team2GoalNumber++
                            GoalItem(goal, false)
                        }
                    }
                    
                    Spacer(modifier = Modifier.weight(1f))

                    // Back button
                    Button(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)

                            .padding(top = 16.dp)
                    ) {
                        Text("Go back", color = LocalTextColor.current,)
                    }

                }

            }
        }

    }
}


@Composable
fun GoalItem(goal: Goal, isFirstTeam: Boolean) {
    Column(modifier = Modifier.fillMaxSize(),horizontalAlignment = if (isFirstTeam) Alignment.Start else Alignment.End){
        Column(modifier = Modifier.fillMaxWidth(0.5f), horizontalAlignment = if (isFirstTeam) Alignment.End else Alignment.Start) {
            Row {
                if (isFirstTeam){
                    Text(
                        text = "${goal.matchMinute}'  ",
                        color = LocalTextColor.current
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
                    Text(
                        text = "  ${goal.matchMinute}'",
                        color = LocalTextColor.current
                    )
                }
            }
        }

        if(goal.comment != null){
            Text(
                text = "${goal.goalGetterName}" +
                        "${goal.comment}",
                textAlign = TextAlign.Center,
                color = LocalTextColor.current
            )
        }
        else if(goal.isOwnGoal == true){
            Text(
                text = "${goal.goalGetterName}\n" +
                        "(OG)",
                textAlign = TextAlign.Center,
                color = LocalTextColor.current
            )
        }
        else{
            Text(
                text = "${goal.goalGetterName}",
                textAlign = TextAlign.Center,
                color = LocalTextColor.current
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}


@Composable
fun TextDetailMatchInformation(text: String){
    Text(
        text = text,
        textAlign = TextAlign.Center,
        color = LocalTextColor.current,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}
