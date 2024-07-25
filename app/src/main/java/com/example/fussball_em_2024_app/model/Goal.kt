package com.example.fussball_em_2024_app.model

class Goal(
    var goalID: Int?,
    var scoreTeam1: Int?,
    var scoreTeam2: Int?,
    var matchMinute: Int?,
    var goalGetterID: Int?,
    var goalGetterName: String?,
    var isPenalty: Boolean?,
    var isOwnGoal: Boolean?,
    var isOvertime: Boolean?,
    var comment: String?

) {

    val getGoalGetterName: String
        get() = goalGetterName ?: "unknow goal scorer"
}