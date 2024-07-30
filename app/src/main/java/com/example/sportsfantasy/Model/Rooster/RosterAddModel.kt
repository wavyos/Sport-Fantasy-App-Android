package com.example.sportsfantasy.Model.Rooster

import com.google.gson.annotations.SerializedName

data class rosterAddModel(
    val filter_code: Int,
    val league_id: String,
    val user_id: String,
    @SerializedName("date")
    val date: String
)