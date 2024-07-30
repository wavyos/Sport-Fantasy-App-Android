package com.example.sportsfantasy.Model


import com.google.gson.annotations.SerializedName

data class MyTest(
    @SerializedName("matchup-list")
    val matchupList: List<Matchup>,
    val success: Boolean,
    @SerializedName("week-list")
    val weekList: List<Week>
) {
    data class Matchup(
        @SerializedName("fisrt_team_name")
        val fisrtTeamName: String,
        @SerializedName("fisrt_team_salary")
        val fisrtTeamSalary: String,
        @SerializedName("fisrt_team_user_name")
        val fisrtTeamUserName: String,
        @SerializedName("fisrt_team_weekly_points")
        val fisrtTeamWeeklyPoints: Int,
        @SerializedName("fisrt_team_winlose")
        val fisrtTeamWinlose: String,
        @SerializedName("second_team_name")
        val secondTeamName: String,
        @SerializedName("second_team_salary")
        val secondTeamSalary: String,
        @SerializedName("second_team_user_name")
        val secondTeamUserName: String,
        @SerializedName("second_team_weekly_points")
        val secondTeamWeeklyPoints: Int,
        @SerializedName("second_team_winlose")
        val secondTeamWinlose: String
    )

    data class Week(
        val value: String,
        @SerializedName("week_code")
        val weekCode: Int
    )
}