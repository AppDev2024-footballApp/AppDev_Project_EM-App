package com.example.fussball_em_2024_app.model


data class OpenAIRequest(
    val model: String,
    val messages: List<Message>,
    val max_tokens: Int,
    val temperature: Double
)

data class Message(
    val role: String,
    val content: String
)

data class Choice(
    val message: Message
)

data class OpenAIResponse(
    val choices: List<Choice>
)
