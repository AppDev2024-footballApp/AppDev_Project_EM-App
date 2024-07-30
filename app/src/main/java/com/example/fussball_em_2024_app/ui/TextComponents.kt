package com.example.fussball_em_2024_app.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.example.fussball_em_2024_app.LocalColors

/***
 * File is used to collect all composable Text ressources in one file
 * For example, to only have to change the text color in one function and not in all files
 */

@Composable
fun SimpleText(text: String, modifier: Modifier = Modifier, style: TextStyle? = null){ // can not do TextStyle.Default instead of null because it is not the same as without style
    if(style != null){
        Text(
            text = text,
            color = LocalColors.current.textColor,
            modifier = modifier,
            style = style
        )
    }else{
        Text(
            text = text,
            color = LocalColors.current.textColor,
            modifier = modifier
        )
    }
}

@Composable
fun TextAlignCenter(text: String, modifier: Modifier = Modifier, style: TextStyle? = null){ // can not do TextStyle.Default instead of null because it is not the same as without style
    if(style != null){
        Text(
            text = text,
            color = LocalColors.current.textColor,
            textAlign = TextAlign.Center,
            modifier = modifier,
            style = style
        )
    }else{
        Text(
            text = text,
            color = LocalColors.current.textColor,
            textAlign = TextAlign.Center,
            modifier = modifier
        )
    }
}