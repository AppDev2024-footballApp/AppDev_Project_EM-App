package com.example.fussball_em_2024_app.model

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
    var matchResults: List<MatchResult>,
    var goals: List<Goal>,
    var location: Location,
    var numberOfViewers: Int
    ) {
}