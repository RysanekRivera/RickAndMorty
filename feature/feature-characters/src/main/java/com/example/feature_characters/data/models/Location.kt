package com.example.feature_characters.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(
    @SerializedName("name") val name: String? = null,
    @SerializedName("url") val url: String? = null
) : Parcelable