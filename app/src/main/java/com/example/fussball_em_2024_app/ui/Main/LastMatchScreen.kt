package com.example.fussball_em_2024_app.ui.Main

import androidx.compose.foundation.background
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
import com.example.fussball_em_2024_app.MatchItems
import com.example.fussball_em_2024_app.R
import com.example.fussball_em_2024_app.model.Match
import com.example.fussball_em_2024_app.utils.LightDarkModeHelper

@Composable
fun LastMatchScreen(match: Match, textColor: Color) {
    val isDarkMode = LightDarkModeHelper.isDarkMode(textColor)
    val backgroundColor = if(isDarkMode){
        Color.Gray
    }else{
        Color.White
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(4.dp)
            )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Last Game",
                style = TextStyle(fontWeight = FontWeight.Bold),
                color = textColor,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            MatchItems(match = match, textColor = textColor)
        }
    }
}