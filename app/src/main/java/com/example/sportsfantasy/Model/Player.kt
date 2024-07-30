package com.example.sportsfantasy.Model

import com.google.gson.annotations.SerializedName
import java.util.*

data class Player(

    //val positionListId:Int?,
//    val FilterCode:Int?,
    val player_id:String,
    val pid: Int?,
    val fullname: String,
    val positiontype: String,
    val primaryposition: String,
    val salary: String,
    val salary_percentage: String,
    val FGM: String,
    @SerializedName("3-pt FG")
    val FG: String,
    val FT: String,
    val Reb: String,
    val Ast: String,
    val BLK: String,
    val STL: String,
    val TOV: String,
    val points: String,
    var isEnable:Boolean? = false,
    var headshot: String?
)
