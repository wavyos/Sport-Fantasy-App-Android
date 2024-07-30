package com.example.sportsfantasy.Model.PlayOff


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("groupList")
    val groupList: List<Group>,
    @SerializedName("rountTitle")
    val rountTitle: String
)