package com.example.sportsfantasy.Model.SearchTab

import com.google.gson.annotations.SerializedName

data class searchTabResponse(
    @SerializedName("data")
    val data: List<Data>,
    val success: Boolean
)