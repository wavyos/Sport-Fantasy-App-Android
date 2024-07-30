package com.example.sportsfantasy.Model.Rooster

import com.google.gson.annotations.SerializedName

data class RosterResponse(
    @SerializedName("data")
    val data: List<Data>,
    val success: Boolean
)