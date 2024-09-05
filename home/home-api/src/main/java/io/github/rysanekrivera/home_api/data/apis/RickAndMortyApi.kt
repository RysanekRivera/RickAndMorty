package io.github.rysanekrivera.home_api.data.apis

import io.github.rysanekrivera.home_api.data.models.GetAllCharactersResponse
import io.github.rysanekrivera.home_api.data.models.GetCharacterByIdResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {

    @GET("character")
    suspend fun getAllCharacters(
        @Query("page") page: Int = 1
    ) : Response<GetAllCharactersResponse>

    @GET("character/{characterId}")
    suspend fun getCharacterById(
        @Path("characterId") id: Int
    ): Response<GetCharacterByIdResponse>

}