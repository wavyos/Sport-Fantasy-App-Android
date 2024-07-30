package com.example.sportsfantasy.Model.PlayOff


import com.google.gson.annotations.SerializedName

data class Group(
    @SerializedName("fisrt_team_name")
    val fisrtTeamName: String,
    @SerializedName("fisrt_team_points")
    val fisrtTeamPoints: Int,
    @SerializedName("fisrt_team_salary")
    val fisrtTeamSalary: Int,
    @SerializedName("second_team_name")
    val secondTeamName: String,
    @SerializedName("second_team_points")
    val secondTeamPoints: Int,
    @SerializedName("second_team_salary")
    val secondTeamSalary: Int
)