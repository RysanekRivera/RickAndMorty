package com.example.feature_characters.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Character(
    @SerializedName("id") val id: Int = -1,
    @SerializedName("name") val name: String = "",
    @SerializedName("status") val status: String? = null,
    @SerializedName("species") val species: String? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("gender") val gender: String? = null,
    @SerializedName("origin") val origin: Origin? = null,
    @SerializedName("location") val location: Location? = null,
    @SerializedName("image") val image: String? = null,
    @SerializedName("episode") val episode: List<String>? = null,
    @SerializedName("url") val url: String? = null,
    @SerializedName("created") val created: String? = null
) : Parcelable