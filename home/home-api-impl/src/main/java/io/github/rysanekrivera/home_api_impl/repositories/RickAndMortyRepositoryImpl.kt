package io.github.rysanekrivera.home_api_impl.repositories

import io.github.rysanekrivera.home_api.data.apis.RickAndMortyApi
import io.github.rysanekrivera.home_api.data.models.GetAllCharactersResponse
import io.github.rysanekrivera.home_api.data.models.GetCharacterByIdResponse
import io.github.rysanekrivera.home_api.repositories.RickAndMortyRepository
import retrofit2.Response
import javax.inject.Inject

class RickAndMortyRepositoryImpl @Inject constructor(
    private val api: RickAndMortyApi
): RickAndMortyRepository {

    override suspend fun getAllCharacters(page: Int): Response<GetAllCharactersResponse> = api.getAllCharacters(page)

    override suspend fun getCharacterById(id: Int): Response<GetCharacterByIdResponse> = api.getCharacterById(id)

}