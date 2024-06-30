package com.example.fussball_em_2024_app.model

class TeamInfo(
    val teamInfoId: Int,
    val teamName: String,
    val shortName: String,
    val teamIconUrl: String,
    val points: Int,
    val opponentGoals: Int,
    val goals: Int,
    val matches: Int,
    val won: Int,
    val lost: Int,
    val draw: Int,
    val goalDiff: Int
) {
}