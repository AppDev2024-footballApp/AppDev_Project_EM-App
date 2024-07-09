package com.example.fussball_em_2024_app



import android.util.Log
import com.example.fussball_em_2024_app.model.OpenAIRequest
import com.example.fussball_em_2024_app.model.OpenAIResponse
import com.example.fussball_em_2024_app.model.Message
import okhttp3.OkHttpClient
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.io.IOException

val openAIRetrofit = Retrofit.Builder()
    .baseUrl("https://api.openai.com/v1/")
    .client(
        OkHttpClient.Builder().addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${System.getenv("OPENAI_API_KEY")}")
                .build()
            chain.proceed(request)
        }.build()
    )
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val openAIService = openAIRetrofit.create(OpenAIService::class.java)

interface OpenAIService {
    @Headers("Content-Type: application/json")
    @POST("chat/completions")
    suspend fun getPrediction(@Body request: OpenAIRequest): OpenAIResponse
}

suspend fun getMatchData(prompt: String): OpenAIResponse? {
    val messages = listOf(
        Message(role = "system", content = "You are a helpful assistant that provides structured data in JSON format."),
        Message(role = "user", content = prompt)
    )

    val request = OpenAIRequest(
        model = "gpt-3.5-turbo",  // Verwende ein aktuelles Modell
        messages = messages,
        max_tokens = 150,  // Stelle sicher, dass genügend Tokens für strukturierte Daten verfügbar sind
        temperature = 0.7
    )

    return try {
        openAIService.getPrediction(request)
    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("OpenAIError", "Error fetching data", e)
        when (e) {
            is HttpException -> {
                // HTTP Fehler behandeln
                val errorMsg = "HTTP error: ${e.response()?.errorBody()?.string()}"
                Log.e("OpenAIError", errorMsg)
            }
            is IOException -> {
                // Netzwerkfehler behandeln
                val errorMsg = "Network error: ${e.message}"
                Log.e("OpenAIError", errorMsg)
            }
            else -> {
                // Andere Fehler behandeln
                val errorMsg = "Unknown error: ${e.message}"
                Log.e("OpenAIError", errorMsg)
            }
        }
        null
    }
}