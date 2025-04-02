package com.example.feature_characters.domain.repositories

import com.example.feature_characters.data.models.AllCharactersResponse
import com.example.feature_characters.data.models.CharacterByIdResponse
import retrofit2.Response

interface RickAndMortyRepository {
    suspend fun getAllCharacters(page: Int): Response<AllCharactersResponse>
    suspend fun getCharacterByName(name: String, page: Int?): Response<AllCharactersResponse>
    suspend fun getCharacterById(id: String):  Response<CharacterByIdResponse>
}