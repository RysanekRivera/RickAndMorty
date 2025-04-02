package com.example.feature_characters.data.remote.apis

import com.example.feature_characters.data.models.AllCharactersResponse
import com.example.feature_characters.data.models.CharacterByIdResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {

    @GET("character/")
    suspend fun getAllCharacters(
        @Query("page") page: Int = 1
    ): Response<AllCharactersResponse>

    @GET("character/")
    suspend fun getCharacterByName(
        @Query("page") page: Int? = null,
        @Query("name") name: String
    ): Response<AllCharactersResponse>

    @GET("character/{id}")
    suspend fun getCharacterById(
        @Path("id") id: String
    ): Response<CharacterByIdResponse>

}