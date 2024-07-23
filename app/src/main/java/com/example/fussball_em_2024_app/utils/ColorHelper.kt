package com.example.fussball_em_2024_app.utils

import androidx.compose.ui.graphics.Color

class ColorHelper(
    var textColor: Color,
    var mainBackgroundColor: Color,
    var secondaryBackgroundColor: Color
) {
    constructor():this(Color.Black, Color.LightGray, Color.Gray)
}