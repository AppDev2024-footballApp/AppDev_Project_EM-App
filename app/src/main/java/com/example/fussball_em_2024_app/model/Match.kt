package com.example.fussball_em_2024_app.model

import java.time.LocalDateTime
import java.util.Date

// /getmatchdata/{matchId}
// /getmatchdata/{leagueShortcut}/{leagueSeason}/{groupOrderId}   (as List)
class Match(
    var matchID: Int,
    var matchDateTime: Date,
    var timeZoneID: String,
    var leagueID: Int,
    var leagueName: String,
    var leagueSeason: Int,
    var leagueShortcut: String,
    var matchDateTimeUTC: Date,
    var group: Group,
    var team1: Team,
    var team2: Team,
    var lastUpdateDateTime: Date,
    var matchIsFinished: Boolean,
    var matchResults: List<MatchResult>?,
    var goals: List<Goal>?,
    var location: Location?,
    var numberOfViewers: Int?
    ) {

    val getTeamVsNames: String
        get() {
            if (team1.shortName == null || team1.shortName.isEmpty() || team2.shortName == null || team2.shortName.isEmpty())
                return "${team1.teamName} vs. ${team2.teamName}"
            return "${team1.shortName} vs. ${team2.shortName}"
        }
}