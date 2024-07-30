package com.example.sportsfantasy.Model.PlayerF

import com.google.gson.annotations.SerializedName

data class playerTabResponse(
    @SerializedName("data")
    val data: Data,
    val success: Boolean
)