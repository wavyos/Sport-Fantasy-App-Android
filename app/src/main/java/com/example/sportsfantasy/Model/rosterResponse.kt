package com.example.sportsfantasy.Model

import com.google.gson.annotations.SerializedName

data class rosterResponse(
    @SerializedName("data")
    val data : Data,
    val success: Boolean
)