package com.example.feature_characters.domain.repositories

import com.example.feature_characters.data.remote.apis.RickAndMortyApi
import javax.inject.Inject

class RickAndMortyRepositoryImpl @Inject constructor(
    private val api: RickAndMortyApi
): RickAndMortyRepository {

    override suspend fun getAllCharacters(page: Int) = api.getAllCharacters(page)
    override suspend fun getCharacterByName(name: String, page: Int?) = api.getCharacterByName(page, name)
    override suspend fun getCharacterById(id: String) = api.getCharacterById(id)

}