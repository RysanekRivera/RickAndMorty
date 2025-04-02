package com.example.feature_characters.data.models

import com.google.gson.annotations.SerializedName

data class Info(
    @SerializedName("count") val count: Int? = null,
    @SerializedName("next") val next: String? = null,
    @SerializedName("pages") val pages: Int? = null,
    @SerializedName("prev") val prev: String? = null
)