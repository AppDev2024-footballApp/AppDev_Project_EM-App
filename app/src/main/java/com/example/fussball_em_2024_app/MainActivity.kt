package com.example.fussball_em_2024_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fussball_em_2024_app.ui.TeamDetail.TeamDetailScreen
import com.example.fussball_em_2024_app.viewModels.TeamViewModel
import com.example.testjetpackcompose.ui.theme.TestJetpackComposeTheme
import okhttp3.Response
import org.json.JSONArray

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FussballEMApp()
        }

    }
}

@Composable
fun FussballEMApp(){
    TestJetpackComposeTheme {
        var currentScreen: FussballDestination by remember { mutableStateOf(Overview) }
        val navController = rememberNavController()

        NavHost(navController, startDestination = Overview.route) {
            composable(route = Overview.route){
                MatchScreen()
            }

            composable(
                route = TeamDetail.routeWithArgs,
                arguments = TeamDetail.arguments){
                TeamDetailScreen(teamId = 6169)
            }

            /*composable("team_screen") {
                val teamViewModel: TeamViewModel = viewModel()
                val viewState by teamViewModel.teamState

                TeamScreen(
                    teams = viewState.list,
                    onTeamClick = { team ->
                        navController.navigate("team_detail_screen/${team.teamId}")
                    }
                )
            }
            composable(
                "team_detail_screen/{teamId}",
                arguments = listOf(navArgument("teamId") { type = NavType.IntType })
            ) { backStackEntry ->
                val teamId = backStackEntry.arguments?.getInt("teamId") ?: 0
                if (teamId != 0) {
                    TeamDetailScreen(teamId = teamId)
                } else {
                    Text("Invalid Team ID")
                }
            }*/
        }
        /*Box(Modifier.padding(innerPadding)) {

        }*/
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            //MatchScreen()
            TeamDetailScreen(teamId = 6169)
            //currentScreen.screen()
        }
    }
}




