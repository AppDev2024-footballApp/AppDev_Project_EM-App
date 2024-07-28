package com.example.fussball_em_2024_app.ui.MatchDetail

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.fussball_em_2024_app.model.OpenAIResponse
import com.example.fussball_em_2024_app.network.getMatchData
import com.example.fussball_em_2024_app.ui.BasicButton
import com.example.fussball_em_2024_app.ui.SimpleText
import com.example.fussball_em_2024_app.ui.TeamFlagImage
import com.example.fussball_em_2024_app.ui.TextAlignCenter
import com.example.fussball_em_2024_app.utils.DateFormater
import com.example.fussball_em_2024_app.viewModels.MatchDetailViewModel
import com.example.fussball_em_2024_app.viewModels.MatchDetailViewModelFactory
import kotlinx.coroutines.launch
import org.json.JSONObject

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

    var prediction by remember { mutableStateOf("Loading...") }
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    Box(modifier = modifier.fillMaxSize()) {
    // Fetch prediction
    LaunchedEffect(matchInfo.match) {
        matchInfo.match?.let { match ->
            val team1Name = match.team1.teamName
            val team2Name = match.team2.teamName
            coroutineScope.launch {
                try {
                    val prompt = """
                    Generate a JSON object for the expected outcome of the match between $team1Name and $team2Name. The Prediction should be based on the last 4 matches between the  teams. The JSON should have the following structure:
                    {
                        "team1": "$team1Name",
                        "team2": "$team2Name",
                        "expectedOutcome": {
                            "team1Score": <integer>,
                            "team2Score": <integer>
                            
                        }
                    }
                    """.trimIndent()
                    val data: OpenAIResponse? = getMatchData(prompt)
                    val jsonString = data?.choices?.firstOrNull()?.message?.content ?: "{}"
                    Log.d("OpenAIResponse", jsonString)  // Log the response
                    val jsonObject = JSONObject(jsonString)
                    val team1 = jsonObject.optString("team1", "Unknown")
                    val team2 = jsonObject.optString("team2", "Unknown")
                    val expectedOutcome = jsonObject.optJSONObject("expectedOutcome")
                    val team1Score = expectedOutcome?.optInt("team1Score", 0) ?: 0
                    val team2Score = expectedOutcome?.optInt("team2Score", 0) ?: 0


                    prediction = "$team1 $team1Score - $team2Score $team2"
                } catch (e: Exception) {
                    e.printStackTrace()
                    prediction = "Error fetching prediction: ${e.message}"
                }
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        when {
            matchInfo.error != null || matchInfo.match == null -> {
                SimpleText("ERROR OCCURRED")
            }

            else -> {
                val match = matchInfo.match!!

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextAlignCenter(
                        text = "Game: ${match.getTeamVsNames}",
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Column(horizontalAlignment = Alignment.Start) {
                            TeamFlagImage(team = match.team1)
                        }

                        Column {
                            TextAlignCenter(
                                text = (if(match.group?.groupName?.length!! > 1) match.group.groupName else "Group ${match.group.groupName}"),
                                style = TextStyle(fontSize = 18.sp),
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            TeamFlagImage(team = match.team2)
                        }
                    }

                    if (match.matchIsFinished == true) {
                        TextDetailMatchInformation(text = "Finished\n" + "Started: ${DateFormater.formatDate(match.matchDateTime)}")
                    }
                    else if(DateFormater.isDateAfterNow(match.matchDateTimeUTC)){
                        TextDetailMatchInformation(text = "Ongoing")
                    }
                    else{
                        TextDetailMatchInformation(text = "Not Started\n" + "Starting at: ${DateFormater.formatDate(match.matchDateTime)}")
                    }

                    if(match.location != null){
                        if(match.location!!.locationStadium == null){
                            TextDetailMatchInformation(text = "Stadion: ${match.location?.locationCity}")
                        }else if(match.location!!.locationCity == null){
                            TextDetailMatchInformation(text = "Stadion: ${match.location?.locationStadium}")
                        }else{
                            TextDetailMatchInformation(text = "Stadion: ${match.location?.locationStadium} (${match.location?.locationCity})")
                        }
                    }

                    if (match.numberOfViewers != null){
                        TextDetailMatchInformation(text = "Number of Viewers: ${match.numberOfViewers}")
                    }


                    if (match.matchIsFinished) {
                        val team1Score = match.goals?.lastOrNull()?.scoreTeam1 ?: 0
                        val team2Score = match.goals?.lastOrNull()?.scoreTeam2 ?: 0
                        TextAlignCenter(
                            text = "Result: ${match.team1.teamName} $team1Score - $team2Score ${match.team2.teamName}",
                            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }


                    // Display prediction
                    TextAlignCenter(
                        text = "Prediction: $prediction",
                        style = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 16.sp),
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    // goals
                    GoalsScreen(match = match)

                    Spacer(modifier = Modifier.weight(1f))

                    // Back button
                    BasicButton("Go back", Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp))
                    {
                        navController.popBackStack()
                    }
                }
            }
        }

    }
}}

@Composable
fun TextDetailMatchInformation(text: String){
    TextAlignCenter(
        text = text,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}
