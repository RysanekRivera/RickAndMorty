package io.github.rysanekrivera.home_api.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Origin(
    @SerialName("name") val name: String,
    @SerialName("url") val url: String
)