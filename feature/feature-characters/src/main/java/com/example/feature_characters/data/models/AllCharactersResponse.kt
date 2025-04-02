package com.example.feature_characters.data.models

import androidx.core.net.toUri
import com.google.gson.annotations.SerializedName

data class AllCharactersResponse(
    @SerializedName("info") val info: Info,
    @SerializedName("results") val characters: List<Character>
){
    val hasNextPage
        get() = info.next != null

    fun extractNextPage() = info.next?.let { it.toUri().getQueryParameter("page")?.toIntOrNull() }

}