package com.example.fussball_em_2024_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.testjetpackcompose.ui.theme.TestJetpackComposeTheme
import com.example.fussball_em_2024_app.network.CallbackCreator
import com.example.fussball_em_2024_app.network.HttpClient
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
                    Greeting("Android")
                }
            }
        }

        // bsp. Get Request
        HttpClient.get("getmatchdata/em/2024",
            CallbackCreator().createCallback(
                ::onFailure,
                ::onResponse
            ))
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

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TestJetpackComposeTheme {
        Greeting("Android")
    }
}