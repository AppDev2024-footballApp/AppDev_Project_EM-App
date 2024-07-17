package com.example.fussball_em_2024_app.model

class League(
    var leagueId: Int,
    var leagueName: String,
    var leagueShortcut: String,
    var leagueSeason: String,
    var sport: Sport
) {
    constructor(leagueId: Int, leagueShortcut: String, leagueSeason: String):this(leagueId, "", leagueShortcut, leagueSeason, Sport(0, ""))
}