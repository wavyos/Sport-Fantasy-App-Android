package com.example.sportsfantasy.Model.PlayerF

import com.google.gson.annotations.SerializedName

data class PositionAll(
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
    val game_points: String,
    val pid: String,
    val player_id: String,
    val positiontype: String,
    val primaryposition: String,
    val salary: String,
    val points: String,
    val salary_percentage: String
)