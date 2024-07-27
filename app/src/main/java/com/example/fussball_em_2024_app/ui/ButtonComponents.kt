package com.example.fussball_em_2024_app.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.fussball_em_2024_app.LocalColors

@Composable
fun BasicButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit){
    Button(
        onClick = { onClick() },
        modifier = modifier
    ) {
        SimpleText(text)
    }
}

@Composable
fun ButtonWithAscendingIcon(text: String, isAscending: Boolean, onClick: () -> Unit){
    Button(onClick = { onClick() }) {
        SimpleText(text)
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