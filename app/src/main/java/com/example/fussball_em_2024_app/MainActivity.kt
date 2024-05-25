package com.example.fussball_em_2024_app

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fussball_em_2024_app.network.CallbackCreator
import com.example.fussball_em_2024_app.network.HttpClient
import okhttp3.Response
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    private lateinit var responseTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        responseTextView = findViewById(R.id.tvApiResponse)

        // bsp. Get Request
        HttpClient.get("getmatchdata/em/2024",
            CallbackCreator().createCallback(
                ::onFailure,
                ::onResponse
            ))
    }

    private fun onResponse(response: Response){
        if(response.isSuccessful.not())
            onFailure()

        val body = response.body?.string()
        if (body != null){
            val jsonBody = JSONArray(body)
            // TODO: best case: parse body to a class
            // TODO: build layout and fill it with the data
            runOnUiThread{
                responseTextView.text = jsonBody.get(0).toString()
            }
        }else{
            runOnUiThread{
                responseTextView.text = "successful call"
            }
        }
    }

    private fun onFailure(){
        runOnUiThread{
            responseTextView.text = "unsuccessful call"
        }
    }
}