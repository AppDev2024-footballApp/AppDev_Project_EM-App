package com.example.fussball_em_2024_app.ui.TeamDetail

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import com.example.fussball_em_2024_app.getMatchData

import com.example.fussball_em_2024_app.model.OpenAIResponse
import com.example.fussball_em_2024_app.viewModels.LastMatchesFactory
import com.example.fussball_em_2024_app.viewModels.LastMatchesViewModel
import com.example.fussball_em_2024_app.viewModels.MatchDetailViewModel
import com.example.fussball_em_2024_app.viewModels.MatchDetailViewModelFactory
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MatchDetailScreen(matchId: Int, navController: NavController, modifier: Modifier = Modifier) {
    val matchDetailViewModel: MatchDetailViewModel = viewModel(
        factory = MatchDetailViewModelFactory(matchId)
    )
    val matchDetail by matchDetailViewModel.matchState

    var prediction by remember { mutableStateOf("Loading...") }
    val coroutineScope = rememberCoroutineScope()

    // Fetch match details and last matches
    val team1MatchesViewModel: LastMatchesViewModel = viewModel(
        factory = LastMatchesFactory(matchDetail.match?.team1?.teamName ?: "", 4)
    )
    val team2MatchesViewModel: LastMatchesViewModel = viewModel(
        factory = LastMatchesFactory(matchDetail.match?.team2?.teamName ?: "", 4)
    )

    val team1Matches = team1MatchesViewModel.matchState.value
    val team2Matches = team2MatchesViewModel.matchState.value

    // Fetch prediction
    coroutineScope.launch {
        if (!team1Matches.loading && !team2Matches.loading && team1Matches.error == null && team2Matches.error == null) {
            val prompt = """
            Provide a detailed prediction for the upcoming match between ${matchDetail.match?.team1?.teamName} and ${matchDetail.match?.team2?.teamName} based on the performance in their last four matches. Include any relevant context such as key players, injuries, and historical performance. The prediction should be in the following JSON format:

            {
                "team1": "${matchDetail.match?.team1?.teamName}",
                "team2": "${matchDetail.match?.team2?.teamName}",
                "expectedOutcome": {
                    "team1Score": <integer>,
                    "team2Score": <integer>,
                    "summary": <string>
                }
            }

            Team 1 last four matches:
            1. ${team1Matches.list.getOrNull(0)?.team1} ${team1Matches.list.getOrNull(0)?.goals?.lastOrNull()?.scoreTeam1}-${team1Matches.list.getOrNull(0)?.goals?.lastOrNull()?.scoreTeam2} ${team1Matches.list.getOrNull(0)?.team2}
            2. ${team1Matches.list.getOrNull(1)?.team1} ${team1Matches.list.getOrNull(1)?.goals?.lastOrNull()?.scoreTeam1}-${team1Matches.list.getOrNull(1)?.goals?.lastOrNull()?.scoreTeam2} ${team1Matches.list.getOrNull(1)?.team2}
            3. ${team1Matches.list.getOrNull(2)?.team1} ${team1Matches.list.getOrNull(2)?.goals?.lastOrNull()?.scoreTeam1}-${team1Matches.list.getOrNull(2)?.goals?.lastOrNull()?.scoreTeam2} ${team1Matches.list.getOrNull(2)?.team2}
            4. ${team1Matches.list.getOrNull(3)?.team1} ${team1Matches.list.getOrNull(3)?.goals?.lastOrNull()?.scoreTeam1}-${team1Matches.list.getOrNull(3)?.goals?.lastOrNull()?.scoreTeam2} ${team1Matches.list.getOrNull(3)?.team2}

            Team 2 last four matches:
            1. ${team2Matches.list.getOrNull(0)?.team1} ${team2Matches.list.getOrNull(0)?.goals?.lastOrNull()?.scoreTeam1}-${team2Matches.list.getOrNull(0)?.goals?.lastOrNull()?.scoreTeam2} ${team2Matches.list.getOrNull(0)?.team2}
            2. ${team2Matches.list.getOrNull(1)?.team1} ${team2Matches.list.getOrNull(1)?.goals?.lastOrNull()?.scoreTeam1}-${team2Matches.list.getOrNull(1)?.goals?.lastOrNull()?.scoreTeam2} ${team2Matches.list.getOrNull(1)?.team2}
            3. ${team2Matches.list.getOrNull(2)?.team1} ${team2Matches.list.getOrNull(2)?.goals?.lastOrNull()?.scoreTeam1}-${team2Matches.list.getOrNull(2)?.goals?.lastOrNull()?.scoreTeam2} ${team2Matches.list.getOrNull(2)?.team2}
            4. ${team2Matches.list.getOrNull(3)?.team1} ${team2Matches.list.getOrNull(3)?.goals?.lastOrNull()?.scoreTeam1}-${team2Matches.list.getOrNull(3)?.goals?.lastOrNull()?.scoreTeam2} ${team2Matches.list.getOrNull(3)?.team2}

            Based on this information, provide a detailed prediction.
            """.trimIndent()

            val data: OpenAIResponse? = getMatchData(prompt)

            if (data != null) {
                try {
                    val jsonResponse = JSONObject(data.choices.firstOrNull()?.message?.content ?: "")
                    val team1Score = jsonResponse.getJSONObject("expectedOutcome").getInt("team1Score")
                    val team2Score = jsonResponse.getJSONObject("expectedOutcome").getInt("team2Score")
                    val summary = jsonResponse.getJSONObject("expectedOutcome").getString("summary")

                    prediction = """
                        Team 1 Score: $team1Score
                        Team 2 Score: $team2Score
                        Summary: $summary
                    """.trimIndent()
                } catch (e: JSONException) {
                    prediction = "Failed to parse prediction: ${e.message}"
                }
            } else {
                prediction = "No prediction available"
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            matchDetail.error != null -> {
                Text("ERROR OCCURRED")
            }
            matchDetail.loading -> {
                Text("Loading...")
            }
            else -> {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Display match details and prediction...
                    Text(
                        text = "Prediction: $prediction",
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    // Teams' logos and group
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        matchDetail.match?.team1?.teamIconUrl?.let { logoUrl ->
                            Image(
                                painter = rememberAsyncImagePainter(logoUrl),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .aspectRatio(1f),
                                contentScale = ContentScale.Crop
                            )
                        }

                        matchDetail.match?.team2?.teamIconUrl?.let { logoUrl ->
                            Image(
                                painter = rememberAsyncImagePainter(logoUrl),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .aspectRatio(1f),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    // Match status
                    val matchFinish = if (matchDetail.match?.matchIsFinished == true) "Finish" else "Not Finish"
                    Text(
                        text = "Status: $matchFinish",
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    // Estimated result
                    Text(
                        text = "Estimated Result: $prediction",
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    // Match result
                    if (matchDetail.match?.matchIsFinished == true) {
                        Text(
                            text = "Result: ${matchDetail.match?.team1?.teamName} ${matchDetail.match?.goals?.lastOrNull()?.scoreTeam1} - ${matchDetail.match?.goals?.lastOrNull()?.scoreTeam2} ${matchDetail.match?.team2?.teamName}",
                            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Goals table
                    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Goals",
                            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        LazyColumn {
                            items(matchDetail.match?.goals ?: emptyList()) { goal ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${goal.goalGetterName}",
                                        style = TextStyle(fontSize = 16.sp),
                                        modifier = Modifier.weight(2f),
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        text = "${goal.matchMinute}'",
                                        style = TextStyle(fontSize = 16.sp),
                                        modifier = Modifier.weight(1f),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f)) // Fills remaining space

                    // Back button
                    Button(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.align(Alignment.Start)
                    )
                    {
                        Text("← Back")
                    }
                }
            }
        }
    }
}
