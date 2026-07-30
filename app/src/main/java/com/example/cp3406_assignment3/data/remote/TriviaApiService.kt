package com.example.studybuddy.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

// Open Trivia DB: https://opentdb.com/api_config.php
interface TriviaApiService {
    @GET("api.php")
    suspend fun getQuestions(
        @Query("amount") amount: Int,
        @Query("category") category: Int? = null, // 18 = Computer Science
        @Query("difficulty") difficulty: String? = null, // easy|medium|hard
        @Query("type") type: String = "multiple"
    ): TriviaResponse
}

data class TriviaResponse(
    val response_code: Int,
    val results: List<TriviaQuestionDto>
)

data class TriviaQuestionDto(
    val category: String,
    val type: String,
    val difficulty: String,
    val question: String,
    val correct_answer: String,
    val incorrect_answers: List<String>
)
