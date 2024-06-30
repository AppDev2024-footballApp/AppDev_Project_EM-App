package com.example.fussball_em_2024_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.example.testjetpackcompose.ui.theme.TestJetpackComposeTheme
import okhttp3.Response
import org.json.JSONArray

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestJetpackComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MatchScreen()
                }
            }
        }

}

private fun onResponse(response: Response){
    if(response.isSuccessful.not())
        onFailure()

    val body = response.body?.string()
    // TODO: impl finish onResponse
    if (body != null){
        val jsonBody = JSONArray(body)
        // TODO: best case: parse body to a class
        // TODO: build layout and fill it with the data

    }else{

    }
}

private fun onFailure(){
    // TODO: impl onFailure
}

}




