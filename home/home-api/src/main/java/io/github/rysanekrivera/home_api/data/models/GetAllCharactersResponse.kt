package io.github.rysanekrivera.home_api.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetAllCharactersResponse(
    @SerialName("info") val info: Info? = null,
    @SerialName("results")val results: List<Result> = emptyList()
)