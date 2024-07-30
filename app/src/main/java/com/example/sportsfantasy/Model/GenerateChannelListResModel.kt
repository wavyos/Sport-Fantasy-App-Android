package com.example.sportsfantasy.Model
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
class GenerateChannelListResModel(
    @SerializedName("conv_list")
    var convList: List<Conv?>?,
    var success: Boolean? // true
) : Parcelable {
    @Parcelize
    class Conv(
        @SerializedName("channel_id")
        var channelId: Int?, // 5
        @SerializedName("chat_type")
        var chatType: String?, // 1
        @SerializedName("last_message")
        var lastMessage: String?, // Hii
        @SerializedName("last_message_time")
        var lastMessageTime: String?, // 2022-12-24 07:07:14
        @SerializedName("last_sender_name")
        var lastSenderName: String?, // Billie Bucher
        @SerializedName("league_id")
        var leagueId: Int?, // 2
        var title: String?, // The CBA Group
        @SerializedName("user_id")
        var userId: Int? // 0
    ) : Parcelable
}