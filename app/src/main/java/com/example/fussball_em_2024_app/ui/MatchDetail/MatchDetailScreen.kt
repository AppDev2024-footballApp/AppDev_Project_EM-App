package com.example.fussball_em_2024_app.ui.MatchDetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.fussball_em_2024_app.R
import com.example.fussball_em_2024_app.model.Goal
import com.example.fussball_em_2024_app.utils.DateFormater
import com.example.fussball_em_2024_app.viewModels.MatchDetailViewModel
import com.example.fussball_em_2024_app.viewModels.MatchDetailViewModelFactory
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.Date

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

    // Scroll state for vertical scrolling
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            matchInfo.error != null || matchInfo.match == null -> {
                Text("ERROR OCCURRED")
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
                    // Title
                    Text(
                        text = "Game: ${match?.team1?.shortName} vs. ${match?.team2?.shortName}",
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp),
                        textAlign = TextAlign.Center,
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
                            // Group
                            Text(
                                text = (if(match?.group?.groupName?.length!! > 1) match.group.groupName else "Group ${match.group.groupName}"),
                                style = TextStyle(fontSize = 18.sp),
                                textAlign = TextAlign.Center,
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

                    // Match State and Icon
                    if (match?.matchIsFinished == true) {
                        Text(
                            text = "Finished\n" +
                                    "Started: ${DateFormater.formatDate(match.matchDateTime)}",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                    else if(DateFormater.isDateAfterNow(match!!.matchDateTimeUTC)){
                        Text(
                            text = "Ongoing",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                    else{
                        Text(
                            text = "Not Started\n" +
                                    "Starting at: ${DateFormater.formatDate(match.matchDateTime)}",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }

                    Text(
                        text = "Stadion: ${match.location?.locationStadium} (${match.location?.locationCity})",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    if (match.numberOfViewers != null){
                        Text(
                            text = "Number of Viewers: ${match.numberOfViewers}",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }

                    // goals
                    //MatchGoalsList(teamName1 = match.team1.teamName, teamName2 = match.team2.teamName, )
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.Start) {
                            Text(match.team1.teamName)
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(match.team2.teamName)
                        }
                    }

                    HorizontalDivider(color = Color.Black, thickness = 3.dp)

                    Spacer(modifier = Modifier.height(15.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                            horizontalArrangement = Arrangement.Absolute.SpaceAround
                        ) {
                            var team1GoalNumber = 0
                            var team2GoalNumber = 0

                            Column(horizontalAlignment = Alignment.Start, ) {
                                match.goals!!.forEach { goal ->
                                    if (goal.scoreTeam1!! > team1GoalNumber){
                                        team1GoalNumber++
                                        GoalItem(goal)
                                    }
                                    else{
                                        GoalItem(null)
                                    }
                                }
                            }


                            VerticalDivider(color = Color.Black, thickness = 2.dp)


                            Column(horizontalAlignment = Alignment.End) {
                                match.goals!!.forEach { goal ->
                                    if (goal.scoreTeam2!! > team2GoalNumber){
                                        team2GoalNumber++
                                        GoalItem(goal)
                                    }
                                    else{
                                        GoalItem(null)
                                    }
                                }
                            }
                        }


                    // Back button
                    Button(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)

                            .padding(top = 16.dp)
                    ) {
                        Text("Go back")
                    }

                }

            }
        }

    }
}


@Composable
fun GoalItem(goal: Goal?) {
    if(goal == null){
        Spacer(modifier = Modifier.height(48.dp))
    }else{
        Row(horizontalArrangement = Arrangement.End) {
            Text(
                text = "${goal.matchMinute}'",
            )
        }
        Row {
            Image(
                painter = painterResource(id = R.drawable.football),
                contentDescription = "football"
            )
            if(goal.goalGetterName!!.length > 15){
                Text(
                    text = "${goal.goalGetterName}",
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp
                )
            }
            else{
                Text(
                    text = "${goal.goalGetterName}",
                    textAlign = TextAlign.Center,
                )
            }
        }

    }
    Spacer(modifier = Modifier.height(24.dp))

}
