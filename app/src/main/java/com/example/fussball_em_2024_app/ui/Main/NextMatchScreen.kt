package com.example.fussball_em_2024_app.ui.Main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fussball_em_2024_app.LocalTextColor
import com.example.fussball_em_2024_app.MatchDetail
import com.example.fussball_em_2024_app.MatchItems
import com.example.fussball_em_2024_app.model.Match
import com.example.fussball_em_2024_app.utils.LightDarkModeHelper

@Composable
fun NextMatchScreen(match: Match, navController: NavController) {
    val isDarkMode = LightDarkModeHelper.isDarkMode(LocalTextColor.current)
    val backgroundColor = if(isDarkMode){
        Color.Gray
    }else{
        Color.White
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(4.dp)) // Abgerundete Ecke und weißer Hintergrund
            .clickable {
                navController.navigate("${MatchDetail.route}/${match.matchID}")
            }
    ){
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "Next Game:",
                style = TextStyle(fontWeight = FontWeight.Bold),
                color = LocalTextColor.current,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            MatchItems(match = match)
        }
    }
}