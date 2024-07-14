package com.example.fussball_em_2024_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fussball_em_2024_app.ui.MatchDetail.MatchDetailScreen
import com.example.fussball_em_2024_app.ui.LightSensorViewModel
import com.example.fussball_em_2024_app.ui.TeamDetail.TeamDetailScreen
import com.example.testjetpackcompose.ui.theme.TestJetpackComposeTheme

var LocalTextColor = staticCompositionLocalOf { Color.Black }

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

    val isInDarkTheme = lightLevel != null && lightLevel!! < 1000 // Lux level to switch mode

    val backgroundColor = if (isInDarkTheme) Color.DarkGray else Color.LightGray
    val textColor = if (isInDarkTheme) Color.White else Color.Black

    val modifier: Modifier = Modifier.background(backgroundColor)

    CompositionLocalProvider(LocalTextColor provides textColor) {
        TestJetpackComposeTheme {
            val navController = rememberNavController()

            NavHost(navController, startDestination = Overview.route) {
                composable(route = Overview.route){
                    MatchScreen(navController, modifier)
                }

                composable(
                    route = TeamDetail.routeWithArgs,
                    arguments = TeamDetail.arguments){ backStackEntry ->
                    val teamId = backStackEntry.arguments?.getInt(TeamDetail.teamIdArg) ?: 0
                    TeamDetailScreen(teamId = teamId, navController)
                }

                composable(
                    route = MatchDetail.routeWithArgs,
                    arguments = MatchDetail.arguments){ backStackEntry ->
                    val matchId = backStackEntry.arguments?.getInt(MatchDetail.matchIdArg) ?: 0
                    MatchDetailScreen(matchId = matchId, navController)
                }
            }
        }
    }
}
