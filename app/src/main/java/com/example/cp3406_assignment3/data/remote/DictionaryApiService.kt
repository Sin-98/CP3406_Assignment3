package com.example.cp3406_assignment3.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

// Free Dictionary API: https://dictionaryapi.dev
interface DictionaryApiService {
    @GET("api/v2/entries/en/{word}")
    suspend fun lookup(@Path("word") word: String): List<DictionaryEntryDto>
}

data class DictionaryEntryDto(
    val word: String,
    val meanings: List<MeaningDto>
)

data class MeaningDto(
    val partOfSpeech: String,
    val definitions: List<DefinitionDto>
)

data class DefinitionDto(
    val definition: String
)
