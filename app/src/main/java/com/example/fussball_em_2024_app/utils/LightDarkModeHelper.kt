package com.example.fussball_em_2024_app.utils

import androidx.compose.ui.graphics.Color

object LightDarkModeHelper {

    fun isDarkMode(textColor: Color) : Boolean{
        return !(textColor == Color.Black || textColor == Color.DarkGray)
    }
}