package com.example.fussball_em_2024_app

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.fussball_em_2024_app.LeagueDetail.leagueSeasonArg
import com.example.fussball_em_2024_app.LeagueDetail.leagueShortcutArg
import com.example.fussball_em_2024_app.TeamDetail.teamIdArg

interface FussballDestination {
    val route: String
}

object Overview : FussballDestination {
    override val route = "overview"
}

object LeagueDetail : FussballDestination {
    override val route = "league_detail"
    const val leagueIdArg = "league_id"
    const val leagueShortcutArg = "league_shortcut"
    const val leagueSeasonArg = "league_season"
    val routeWithArgs = "${route}/{$leagueIdArg}/{$leagueShortcutArg}/{$leagueSeasonArg}"
    val arguments = listOf(
        navArgument(leagueIdArg) { type = NavType.IntType},
        navArgument(leagueShortcutArg) { type = NavType.StringType},
        navArgument(leagueSeasonArg) { type = NavType.StringType}
    )
}

object TeamDetail : FussballDestination {
    override val route = "team_detail"
    const val leagueIdArg = "league_id"
    const val leagueShortcutArg = "league_shortcut"
    const val leagueSeasonArg = "league_season"
    const val teamIdArg = "team_id"
    val routeWithArgs = "${route}/{${leagueIdArg}}/{$leagueShortcutArg}/{$leagueSeasonArg}/{${teamIdArg}}"
    val arguments = listOf(
        navArgument(leagueIdArg) { type = NavType.IntType},
        navArgument(leagueShortcutArg) { type = NavType.StringType},
        navArgument(leagueSeasonArg) { type = NavType.StringType},
        navArgument(teamIdArg) { type = NavType.IntType }
    )
}

object MatchDetail : FussballDestination {
    override val route = "match_detail"
    const val matchIdArg = "match_id"
    val routeWithArgs = "${route}/{${matchIdArg}}"
    val arguments = listOf(navArgument(matchIdArg) { type = NavType.IntType })
}