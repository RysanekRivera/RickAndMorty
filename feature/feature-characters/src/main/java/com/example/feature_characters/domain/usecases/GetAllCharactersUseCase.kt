package com.example.feature_characters.domain.usecases

import com.example.feature_characters.data.models.AllCharactersResponse
import com.example.feature_characters.domain.repositories.RickAndMortyRepository
import retrofit2.Response
import javax.inject.Inject

class GetAllCharactersUseCase @Inject constructor(
    private val repository: RickAndMortyRepository
) {

    suspend fun fetchData(page: Int = 1): Response<AllCharactersResponse> = repository.getAllCharacters(page)

}