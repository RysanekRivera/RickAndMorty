package com.example.feature_characters.data.models


data class CharacterByIdResponse(
    val id: Int,
    val name: String,
    val status: String?,
    val species: String?,
    val type: String?,
    val gender: String,
    val origin: Origin,
    val location: Location,
    val image: String?,
    val episode: List<String> = listOf(),
    val url: String?,
    val created: String
)