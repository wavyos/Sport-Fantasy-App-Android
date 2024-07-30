package com.example.sportsfantasy.Model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserComLeagues(
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("deleted_at")
    val deletedAt: String?,
    @SerializedName("entry_fee")
    val entryFee: Int?,
    @SerializedName("first_team_id")
    val firstTeamId: Int?,
    @SerializedName("first_team_score")
    val firstTeamScore: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("invitation_code")
    val invitationCode: String?,
    @SerializedName("invitation_link")
    val invitationLink: String?,
    @SerializedName("is_entry_fee")
    val isEntryFee: String?,
    @SerializedName("is_postseason")
    val isPostseason: String?,
    @SerializedName("league_fill_up")
    val leagueFillUp: String?,
    @SerializedName("league_id")
    val leagueId: Int?,
    @SerializedName("league_name")
    val leagueName: String?,
    @SerializedName("league_size")
    val leagueSize: Int?,
    @SerializedName("league_start_date")
    val leagueStartDate: String?,
    @SerializedName("league_status")
    val leagueStatus: String?,
    @SerializedName("league_type")
    val leagueType: String?,
    @SerializedName("league_week")
    val leagueWeek: Int?,
    @SerializedName("playoff")
    val playoff: String?,
    @SerializedName("rank")
    val rank: List<Rank>?,
    @SerializedName("second_team_id")
    val secondTeamId: Int?,
    @SerializedName("second_team_score")
    val secondTeamScore: Int?,
    @SerializedName("start_date_of_week")
    val startDateOfWeek: String?,
    @SerializedName("tid")
    val tid: Int?,
    @SerializedName("total")
    val total: Int?,
    @SerializedName("totalmatch")
    val totalmatch: Int?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("user_id")
    val userId: Int?,
    @SerializedName("username")
    val username: String?,
    @SerializedName("week_status")
    val weekStatus: String?,
    @SerializedName("win_team_id")
    val winTeamId: Int?,
    @SerializedName("win_team_user_id")
    val winTeamUserId: Int?
) : Parcelable