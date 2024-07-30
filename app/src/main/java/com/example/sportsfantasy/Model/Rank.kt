package com.example.sportsfantasy.Model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Rank(
    @SerializedName("league_id")
    val leagueId: Int?,
    @SerializedName("league_name")
    val leagueName: String?,
    @SerializedName("rank")
    val rank: Int?,
    @SerializedName("total")
    val total: Int?,
    @SerializedName("totalmatch")
    val totalmatch: Int?,
    @SerializedName("user_id")
    val userId: Int?,
    @SerializedName("username")
    val username: String?
) : Parcelable