package com.example.fussball_em_2024_app.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import com.example.fussball_em_2024_app.LocalColors
import com.example.testjetpackcompose.ui.theme.buttonsColor

@Composable
fun BasicButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit){
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonsColor, // Farbe des Buttons
            contentColor = White,
        ),
        modifier = modifier
    ) {
        Text(text = text)
    }
}

@Composable
fun ButtonWithAscendingIcon(text: String, isAscending: Boolean, onClick: () -> Unit){
    Button(
        onClick = { onClick() } ,
        colors = ButtonDefaults.buttonColors(
        containerColor = buttonsColor, // Farbe des Buttons
        contentColor = White,
    ),) {
        Text(text, color = White)
        OrderIcon(isAscending)
    }
}

@Composable
fun OrderIcon(isAscending: Boolean){
    if(isAscending){
        Icon(Icons.Rounded.KeyboardArrowDown, contentDescription = "Arrow Down", tint = LocalColors.current.textColor)
    }
    else{
        Icon(Icons.Rounded.KeyboardArrowUp, contentDescription = "Arrow Up", tint = LocalColors.current.textColor)
    }
}