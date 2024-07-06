package com.example.fussball_em_2024_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fussball_em_2024_app.ui.LightSensorViewModel
import com.example.fussball_em_2024_app.ui.TeamDetail.TeamDetailScreen
import com.example.testjetpackcompose.ui.theme.TestJetpackComposeTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: LightSensorViewModel = viewModel()
            FussballEMApp(lightData = viewModel.lightData)
        }

    }
}

@Composable
fun FussballEMApp(lightData: LiveData<Float>){
    val lightLevel by lightData.observeAsState()

    val isDarkMode = lightLevel != null && lightLevel!! < 1000 // Lux level to switch mode

    val backgroundColor = if (isDarkMode) Color.DarkGray else Color.LightGray
    val textColor = if (isDarkMode) Color.White else Color.Black

    val modifier: Modifier = Modifier.background(backgroundColor)

    TestJetpackComposeTheme {
        val navController = rememberNavController()

        NavHost(navController, startDestination = Overview.route) {
            composable(route = Overview.route){
                MatchScreen(navController, textColor, modifier)
            }

            composable(
                route = TeamDetail.routeWithArgs,
                arguments = TeamDetail.arguments){ backStackEntry ->
                val teamId = backStackEntry.arguments?.getInt(TeamDetail.teamIdArg) ?: 0
                TeamDetailScreen(teamId = teamId, navController, textColor, modifier)
            }
        }
    }
}




