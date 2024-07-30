package com.example.sportsfantasy.Model

import com.google.gson.annotations.SerializedName

data class PlayerX
    (
    val draft_id: Int,
    val fullname: String,
    val match_date: String,
    @SerializedName("salary")
    val salary: String,
    val salary_percentage: String,
    val user_id: String,
    val pid: String,
    var headshot: String?
)