package com.example.sportsfantasy.Model.Rooster

import com.google.gson.annotations.SerializedName

data class LeagueScoreCardAddModel(
    val user_id: Int,
    val league_id: Int,
    val week_code: Int
)