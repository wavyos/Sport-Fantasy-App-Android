package com.example.sportsfantasy.Model


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class TestMatchUp(
    @SerializedName("matchup-list")
    var matchupList: List<Matchup?>?,
    var success: Boolean?, // true
    @SerializedName("week-list")
    var weekList: List<Week?>?
) : Parcelable {
    @Parcelize
    data class Matchup(
        @SerializedName("fisrt_team_name")
        var fisrtTeamName: String?, // jack1 jack1 Team
        @SerializedName("fisrt_team_salary")
        var fisrtTeamSalary: String?, // 100.00
        @SerializedName("fisrt_team_user_name")
        var fisrtTeamUserName: String?, // j.jack1
        @SerializedName("fisrt_team_weekly_points")
        var fisrtTeamWeeklyPoints: Int?, // 0
        @SerializedName("fisrt_team_winlose")
        var fisrtTeamWinlose: String?, // 0-0
        @SerializedName("second_team_name")
        var secondTeamName: String?, // Billie Bucher Team
        @SerializedName("second_team_salary")
        var secondTeamSalary: String?, // 100.00
        @SerializedName("second_team_user_name")
        var secondTeamUserName: String?, // B.Bucher
        @SerializedName("second_team_weekly_points")
        var secondTeamWeeklyPoints: Int?, // 0
        @SerializedName("second_team_winlose")
        var secondTeamWinlose: String? // 0-0
    ) : Parcelable

    @Parcelize
    data class Week(
        var value: String?, // Matchup 1 Dec 19 - Dec 25
        @SerializedName("week_code")
        var weekCode: Int? // 1
    ) : Parcelable
}