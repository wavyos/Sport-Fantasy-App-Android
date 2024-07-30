package com.example.sportsfantasy.Model

import com.google.gson.annotations.SerializedName

data class PlayerXX(
    @SerializedName("3-pt FG")
    val FG: String,
    val Ast: String,
    val BLK: String,
    val FGM: String,
    val FT: String,
    val Reb: String,
    val STL: String,
    val TOV: String,
    val fullname: String,
    val pid: Int,
    val player_id: Int,
    val positiontype: String,
    val primaryposition: String,
    val salary: Int,
    val salary_percentage: Int
)