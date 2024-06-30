package com.example.fussball_em_2024_app

import androidx.navigation.NavType
import androidx.navigation.navArgument

interface FussballDestination {
    val route: String
}

object Overview : FussballDestination {
    override val route = "overview"
}

object TeamDetail : FussballDestination {
    override val route = "team_detail"
    const val teamIdArg = "team_id"
    val routeWithArgs = "${route}/{${teamIdArg}}"
    val arguments = listOf(navArgument(teamIdArg) { type = NavType.IntType })
}