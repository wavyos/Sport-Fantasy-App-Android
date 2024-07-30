package com.example.sportsfantasy.Model.MatchUp

import com.google.gson.annotations.SerializedName

data class MatchUpResponse(
    @SerializedName("data")
    val data: List<Data>,
    val success: Boolean
)