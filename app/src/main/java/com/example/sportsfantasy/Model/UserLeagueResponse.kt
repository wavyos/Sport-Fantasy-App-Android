package com.example.sportsfantasy.Model


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class UserLeagueResponse(
    var success: Boolean?, // true
    @SerializedName("user_leagues")
    var userLeagues: List<UserLeague>,
    @SerializedName("user_com_leagues")
    var userComLeagues: List<UserLeague>
) : Parcelable {
    @Parcelize
    data class UserLeague(
        @SerializedName("created_at")
        var createdAt: String?, // 2022-12-12 12:21:39
        @SerializedName("start_date_of_week")
        var start_date_of_week: String?, // 2022-12-12 12:21:39
        @SerializedName("current_week")
        var currentWeek: Int?, // 3
        @SerializedName("total")
        var total: Int? = 0, // 3
        var isCompleted: Boolean? = true, // 3
        @SerializedName("league_id")
        var league_id: Int? = 0,
        @SerializedName("win_team_user_id")
        var win_team_user_id: Int? = 0,
        @SerializedName("first_team_id")
        var first_team_id: Int? = 0,
        @SerializedName("second_team_id")
        var second_team_id: Int? = 0,
        @SerializedName("totalmatch")
        var totalmatch: Int? = 0,
        @SerializedName("first_team_score")
        var first_team_score: Int? = 0,
        @SerializedName("win_team_id")
        var win_team_id: Int? = 0,
        @SerializedName("second_team_score")
        var second_team_score: Int? = 0,
        @SerializedName("week_status")
        var week_status: String?,
        @SerializedName("playoff")
        var playoff: String?,
        @SerializedName("username")
        var username: String?,
        @SerializedName("deleted_at")
        var deletedAt: String?,
        @SerializedName("entry_fee")
        var entryFee: Int?, // 0
        var id: Int?, // 2
        @SerializedName("invitation_code")
        var invitationCode: String?, // 121222-y7YTd0MvDPmRGJOB
        @SerializedName("invitation_link")
        var invitationLink: String?, // https://api.wavyos.com/public/user/login?league_code=121222-y7YTd0MvDPmRGJOB
        @SerializedName("is_entry_fee")
        var isEntryFee: String?, // 0
        @SerializedName("is_league_admin")
        var isLeagueAdmin: String?, // 1
        @SerializedName("is_postseason")
        var isPostseason: String?, // 0
        @SerializedName("league_fill_up")
        var leagueFillUp: String?, // 0
        @SerializedName("league_name")
        var leagueName: String?, // The CBA
        @SerializedName("draft_active")
        var draftActive: Int?,
        @SerializedName("league_size")
        var leagueSize: Int?, // 12
        @SerializedName("draft_date")
        var draftDate: String?,
        @SerializedName("league_start_date")
        var leagueStartDate: String?, // 2022-12-13 00:00:00
        @SerializedName("league_status")
        var leagueStatus: String?, // 1
        @SerializedName("league_type")
        var leagueType: String?, // 1
        @SerializedName("league_week")
        var leagueWeek: Int?, // 3
        @SerializedName("matchup_details")
        var matchupDetails: List<MatchupDetail?>?,
        @SerializedName("rank")
        var rank:  List<Rank?>?,
        @SerializedName("remaining_members")
        var remainingMembers: Int?, // 9
        var tid: Int?, // 1
        @SerializedName("updated_at")
        var updatedAt: String?, // 2022-12-13 09:41:29
        @SerializedName("user_id")
        var userId: Int? // 114
    ) : Parcelable {
        @Parcelize
        data class MatchupDetail(
            @SerializedName("first_member_name")
            var firstMemberName: String?, // jack1 jack1
            @SerializedName("fisrt_team_name")
            var fisrtTeamName: String?, // jack1 jack1 Team
            @SerializedName("fisrt_team_points")
            var fisrtTeamPoints: String?, // 0.00
            @SerializedName("fisrt_team_winlose")
            var fisrtTeamWinlose: String?, // 0-0
            @SerializedName("second_member_name")
            var secondMemberName: String?, // Billie Bucher
            @SerializedName("second_team_name")
            var secondTeamName: String?, // Billie Bucher Team
            @SerializedName("second_team_points")
            var secondTeamPoints: String?, // 0.00
            @SerializedName("second_team_winlose")
            var secondTeamWinlose: String? // 0-0
        ) : Parcelable
    }
}