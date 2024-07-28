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
import com.example.fussball_em_2024_app.model.League
import com.example.fussball_em_2024_app.ui.leagues.LeaguesScreen
import com.example.fussball_em_2024_app.ui.main.MatchScreen
import com.example.fussball_em_2024_app.ui.matchDetail.MatchDetailScreen
import com.example.fussball_em_2024_app.viewModels.LightSensorViewModel
import com.example.fussball_em_2024_app.ui.teamDetail.TeamDetailScreen
import com.example.fussball_em_2024_app.utils.ColorHelper
import com.example.testjetpackcompose.ui.theme.TestJetpackComposeTheme

val LocalColors = staticCompositionLocalOf { ColorHelper() }
val LocalLeague = staticCompositionLocalOf { League() }

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

    val isInDarkTheme = lightLevel != null && lightLevel!! < 30 // Lux level to switch mode

    val backgroundColor = if (isInDarkTheme) Color.DarkGray else Color.LightGray
    val textColor = if (isInDarkTheme) Color.White else Color.Black
    val secBackColor = if (isInDarkTheme) Color.Gray else Color.White

    val colors = ColorHelper(textColor, backgroundColor, secBackColor)
    val modifier: Modifier = Modifier.background(backgroundColor)

    CompositionLocalProvider(LocalColors provides colors) {
        TestJetpackComposeTheme {
            val navController = rememberNavController()

            NavHost(navController, startDestination = Overview.route) {
                composable(route = Overview.route){
                    LeaguesScreen(navController, modifier)
                }

                composable(
                    route = LeagueDetail.routeWithArgs,
                    arguments = LeagueDetail.arguments){ backStackEntry ->
                    val leagueId = backStackEntry.arguments?.getInt(LeagueDetail.leagueIdArg) ?: 4708
                    val leagueShortcut = backStackEntry.arguments?.getString(LeagueDetail.leagueShortcutArg) ?: "em"
                    val leagueSeasons = backStackEntry.arguments?.getString(LeagueDetail.leagueSeasonArg) ?: "2024"

                    CompositionLocalProvider(value = LocalLeague provides League(leagueId, leagueShortcut, leagueSeasons)) {
                        MatchScreen(navController, modifier)
                    }
                }

                composable(
                    route = TeamDetail.routeWithArgs,
                    arguments = TeamDetail.arguments){ backStackEntry ->
                    val leagueId = backStackEntry.arguments?.getInt(LeagueDetail.leagueIdArg) ?: 4708
                    val leagueShortcut = backStackEntry.arguments?.getString(LeagueDetail.leagueShortcutArg) ?: "em"
                    val leagueSeasons = backStackEntry.arguments?.getString(LeagueDetail.leagueSeasonArg) ?: "2024"
                    val teamId = backStackEntry.arguments?.getInt(TeamDetail.teamIdArg) ?: 0

                    CompositionLocalProvider(value = LocalLeague provides League(leagueId, leagueShortcut, leagueSeasons)){
                        TeamDetailScreen(teamId = teamId, navController, modifier)
                    }
                }

                composable(
                    route = MatchDetail.routeWithArgs,
                    arguments = MatchDetail.arguments){ backStackEntry ->
                    val matchId = backStackEntry.arguments?.getInt(MatchDetail.matchIdArg) ?: 0
                    MatchDetailScreen(matchId = matchId, navController, modifier)
                }
            }
        }
    }
}
