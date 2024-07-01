package com.example.fussball_em_2024_app.ui.Main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.fussball_em_2024_app.entity.FavouriteTeam
import com.example.fussball_em_2024_app.model.Team

@Composable
fun FavouriteTeams(leagueName: String, teams: List<Team>, onTeamClick: (Team) -> Unit,
                   viewModel: FavouriteTeamsViewModel = viewModel(
                       factory = FavouriteTeamsViewModelFactory(LocalContext.current)
                   )){

    LaunchedEffect(leagueName) {
        viewModel.loadFavouriteTeams(leagueName)
    }

    val favouriteTeams by viewModel.favouriteTeams.collectAsState()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Favourite Teams",
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            favouriteTeams.forEach { favTeam ->
                var team: Team? = null
                for(it in teams){
                    if (it.teamName == favTeam.teamName){
                        team = it
                        break
                    }
                }

                if(team != null){
                    Column(
                        modifier = Modifier
                            .clickable { onTeamClick(team) }
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(model = team.teamIconUrl),
                            contentDescription = "Logo von ${team.teamName}",
                            modifier = Modifier
                                .size(60.dp)
                                .aspectRatio(1f)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = team.teamName,
                            textAlign = TextAlign.Center,
                            style = TextStyle(fontSize = 14.sp)
                        )
                    }
                }
            }

            // Add button for adding more teams
            Column(
                modifier = Modifier
                    .clickable { /* Handle Add Team Logic */ }
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .background(Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("+", style = TextStyle(color = Color.White, fontSize = 24.sp))
                }
                Text(
                    text = "Add",
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontSize = 14.sp)
                )
            }
        }
    }
}