package com.example.sportsfantasy.Model.PlayOff


import com.google.gson.annotations.SerializedName

data class PlayOffResponse(
    @SerializedName("data")
    val `playOffData`: ArrayList<Data>,
    @SerializedName("success")
    val success: Boolean
)