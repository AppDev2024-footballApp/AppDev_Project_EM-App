package com.example.fussball_em_2024_app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.fussball_em_2024_app.LocalColors
import com.example.fussball_em_2024_app.R
import com.example.fussball_em_2024_app.model.Team
import com.example.fussball_em_2024_app.model.TeamInfo
import com.example.testjetpackcompose.ui.theme.darkGreen90

@Composable
fun TeamFlagImage(team: Team, size: Dp = 60.dp){
    Image(
        painter = rememberAsyncImagePainter(model = team.teamIconUrl),
        contentDescription = "Logo von ${team.teamName}",
        modifier = Modifier
            .size(size)
            .aspectRatio(1f)
            .clip(CircleShape),  // Macht das Bild kreisförmig
        contentScale = ContentScale.Crop
    )
}

@Composable
fun TeamFlagImage(team: TeamInfo, size: Dp = 60.dp){
    Image(
        painter = rememberAsyncImagePainter(model = team.teamIconUrl),
        contentDescription = "Logo von ${team.teamName}",
        modifier = Modifier
            .size(size)
            .aspectRatio(1f)
            .clip(CircleShape),  // Macht das Bild kreisförmig
        contentScale = ContentScale.Crop
    )
}

@Composable
fun FootballImage(){
    Image(
        painter = if (LocalColors.current.textColor == darkGreen90) painterResource(id = R.drawable.black_football) else painterResource(id = R.drawable.white_football),
        contentDescription = "football"
    )
}