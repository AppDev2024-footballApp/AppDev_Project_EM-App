package com.example.fussball_em_2024_app.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class FavouriteTeam(
    @PrimaryKey val ftid: Int,
    @ColumnInfo(name = "leagueName") val leagueName: String,
    @ColumnInfo(name = "teamName") val teamName: String
)
