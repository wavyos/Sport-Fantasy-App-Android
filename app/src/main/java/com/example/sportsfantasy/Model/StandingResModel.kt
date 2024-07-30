package com.example.sportsfantasy.Model


import com.google.gson.annotations.SerializedName
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
class StandingResModel(
    @SerializedName("standings_data")
    var standingsData: StandingsData?,
    var success: Boolean? // true
) : Parcelable {
    @Parcelize
    class StandingsData(
        @SerializedName("east_teams")
        var eastTeams: List<EastTeam?>?,
        @SerializedName("west_teams")
        var westTeams: List<WestTeam?>?
    ) : Parcelable {
        @Parcelize
        class EastTeam(
            var gb: String?, // -
            @SerializedName("member_side")
            var memberSide: String?, // 0
            var pa: String?, // -
            var pf: String?, // -
            var playoff: String?, // -
            var record: String?, // 0-0-0
            var strk: String?, // -
            @SerializedName("team_name")
            var teamName: String?, // veyac Team
            @SerializedName("team_user_name")
            var teamUserName: String?, // veyac
            var win: String? // -
        ) : Parcelable

        @Parcelize
        class WestTeam(
            var gb: String?, // -
            @SerializedName("member_side")
            var memberSide: String?, // 1
            var pa: String?, // -
            var pf: String?, // -
            var playoff: String?, // -
            var record: String?, // 0-0-0
            var strk: String?, // -
            @SerializedName("team_name")
            var teamName: String?, // Team 2
            @SerializedName("team_user_name")
            var teamUserName: String?, // -
            var win: String? // -
        ) : Parcelable
    }
}