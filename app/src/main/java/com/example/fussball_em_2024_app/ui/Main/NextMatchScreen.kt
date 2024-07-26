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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fussball_em_2024_app.LocalColors
import com.example.fussball_em_2024_app.MatchDetail
import com.example.fussball_em_2024_app.MatchItems
import com.example.fussball_em_2024_app.model.Match
import com.example.fussball_em_2024_app.ui.SimpleText

@Composable
fun NextMatchScreen(match: Match, navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .background(color = LocalColors.current.secondaryBackgroundColor, shape = RoundedCornerShape(4.dp)) // Abgerundete Ecke und weißer Hintergrund
            .clickable {
                navController.navigate("${MatchDetail.route}/${match.matchID}")
            }
    ){
        Column(modifier = Modifier.padding(12.dp)) {
            SimpleText(
                text = "Next Game:",
                style = TextStyle(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            MatchItems(match = match)
        }
    }
}