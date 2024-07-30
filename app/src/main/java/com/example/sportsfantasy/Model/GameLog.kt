package com.example.sportsfantasy.Model

import com.google.gson.annotations.SerializedName

data class GameLog(
    @SerializedName("3-pt FG")
    val FG: String,
    val Ast: String,
    val BLK: String,
    val FGM: String,
    val FT: String,
    val Reb: String,
    val STL: String,
    val TOV: String,
    val date: String,
    val salary: String,
    val points: String,
    val salaryPercentage: String
)