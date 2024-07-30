package com.example.sportsfantasy.Model.NewTenPos


import com.google.gson.annotations.SerializedName

data class PlayerNewTen(
    @SerializedName("Ast")
    val ast: String,
    @SerializedName("BLK")
    val bLK: String,
    @SerializedName("FGM")
    val fGM: String,
    @SerializedName("FT")
    val fT: String,
    @SerializedName("fullname")
    val fullname: String,
    @SerializedName("headshot")
    val headshot: String,
    @SerializedName("pid")
    val pid: Int,
    @SerializedName("player_id")
    val playerId: Int,
    @SerializedName("points")
    val points: String,
    @SerializedName("positiontype")
    val positiontype: String,
    @SerializedName("primaryposition")
    val primaryposition: String,
    @SerializedName("3-pt FG")
    val ptFG: String,
    @SerializedName("Reb")
    val reb: String,
    @SerializedName("STL")
    val sTL: String,
    @SerializedName("salary")
    val salary: Int,
    @SerializedName("salary_percentage")
    val salaryPercentage: Int,
    @SerializedName("TOV")
    val tOV: String
)