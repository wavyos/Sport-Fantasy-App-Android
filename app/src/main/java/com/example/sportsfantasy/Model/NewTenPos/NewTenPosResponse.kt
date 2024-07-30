package com.example.sportsfantasy.Model.NewTenPos


import com.example.sportsfantasy.Model.Player
import com.google.gson.annotations.SerializedName

data class NewTenPosResponse(
    @SerializedName("playerList")
    val playerList: List<Player>,
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("total_items")
    val totalItems: Int
)