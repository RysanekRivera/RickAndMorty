package io.github.rysanekrivera.home_api.repositories

import io.github.rysanekrivera.home_api.data.models.GetAllCharactersResponse
import io.github.rysanekrivera.home_api.data.models.GetCharacterByIdResponse
import retrofit2.Response

interface RickAndMortyRepository {
    suspend fun getAllCharacters(page: Int = 1): Response<GetAllCharactersResponse>
    suspend fun getCharacterById(id: Int): Response<GetCharacterByIdResponse>
}