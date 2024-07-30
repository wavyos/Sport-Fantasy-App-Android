package com.example.sportsfantasy.Model

import com.google.gson.annotations.SerializedName

data class Position(
    val salary:Int,
    val code: String,
    val created_at: String,
    val deleted_at: String,
    val description: String,
    val title: String?,
    val id: Int,
    val updated_at: String,
    @SerializedName("player")
    val player: List<PlayerX>,
    val imagepath: String,
    val points: Any,
)