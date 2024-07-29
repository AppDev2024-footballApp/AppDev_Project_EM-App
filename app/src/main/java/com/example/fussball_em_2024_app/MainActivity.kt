package com.example.fussball_em_2024_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.testjetpackcompose.ui.theme.darkGreen
import com.example.testjetpackcompose.ui.theme.darkGreen90
import com.example.testjetpackcompose.ui.theme.lightBeige
import com.example.testjetpackcompose.ui.theme.lightGreen


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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FussballEMApp(lightData: LiveData<Float>) {
    val lightLevel by lightData.observeAsState()

    val isInDarkTheme = lightLevel != null && lightLevel!! < 5 // Lux level to switch mode

    val backgroundColor = if (isInDarkTheme) Color.DarkGray else lightBeige
    val textColor = if (isInDarkTheme) Color.White else darkGreen90
    val secBackColor = if (isInDarkTheme) Color.Gray else Color.White

    val colors = ColorHelper(textColor, backgroundColor, secBackColor)
    val modifier: Modifier = Modifier.background(backgroundColor)

    CompositionLocalProvider(LocalColors provides colors) {
        TestJetpackComposeTheme {
            val navController = rememberNavController()

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = "SOCCER",
                                color = Color.White,
                                style = TextStyle(
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp,
                                    fontFamily = FontFamily.SansSerif
                                )
                            )
                        },
                        navigationIcon = {
                            Image(
                                painter = painterResource(id = R.drawable.logo),
                                contentDescription = "SOCCER",
                                colorFilter = ColorFilter.tint(Color.White),
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .size(24.dp) // Größe des Logos anpassen
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = darkGreen,
                            titleContentColor = Color.White,
                            navigationIconContentColor = Color.White
                        )
                    )
                },
                content = { padding ->
                    Box(modifier = Modifier.padding(padding)) {
                        NavHost(navController, startDestination = Overview.route) {
                            composable(route = Overview.route) {
                                LeaguesScreen(navController, modifier)
                            }

                            composable(
                                route = LeagueDetail.routeWithArgs,
                                arguments = LeagueDetail.arguments
                            ) { backStackEntry ->
                                val leagueId = backStackEntry.arguments?.getInt(LeagueDetail.leagueIdArg) ?: 4708
                                val leagueShortcut = backStackEntry.arguments?.getString(LeagueDetail.leagueShortcutArg) ?: "em"
                                val leagueSeasons = backStackEntry.arguments?.getString(LeagueDetail.leagueSeasonArg) ?: "2024"

                                CompositionLocalProvider(value = LocalLeague provides League(leagueId, leagueShortcut, leagueSeasons)) {
                                    MatchScreen(navController, modifier)
                                }
                            }

                            composable(
                                route = TeamDetail.routeWithArgs,
                                arguments = TeamDetail.arguments
                            ) { backStackEntry ->
                                val leagueId = backStackEntry.arguments?.getInt(LeagueDetail.leagueIdArg) ?: 4708
                                val leagueShortcut = backStackEntry.arguments?.getString(LeagueDetail.leagueShortcutArg) ?: "em"
                                val leagueSeasons = backStackEntry.arguments?.getString(LeagueDetail.leagueSeasonArg) ?: "2024"
                                val teamId = backStackEntry.arguments?.getInt(TeamDetail.teamIdArg) ?: 0

                                CompositionLocalProvider(value = LocalLeague provides League(leagueId, leagueShortcut, leagueSeasons)) {
                                    TeamDetailScreen(teamId = teamId, navController, modifier)
                                }
                            }

                            composable(
                                route = MatchDetail.routeWithArgs,
                                arguments = MatchDetail.arguments
                            ) { backStackEntry ->
                                val matchId = backStackEntry.arguments?.getInt(MatchDetail.matchIdArg) ?: 0
                                MatchDetailScreen(matchId = matchId, navController, modifier)
                            }
                        }
                    }
                }
            )
        }
    }
}

