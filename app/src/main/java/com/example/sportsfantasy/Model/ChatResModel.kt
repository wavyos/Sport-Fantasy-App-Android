package com.example.sportsfantasy.Model


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
class ChatResModel(
    @SerializedName("channel_id")
    var channelId: Int?, // 5
    var messages: List<Message?>?,
    var success: Boolean? // true
) : Parcelable {
    @Parcelize
    class Message(
        var message: String?, // Hello
        @SerializedName("message_id")
        var messageId: Int?, // 8
        @SerializedName("message_time")
        var messageTime: String?, // 2022-12-23 14:19:49
        @SerializedName("receiver_id")
        var receiverId: Int?, // 0
        @SerializedName("sender_id")
        var senderId: Int?, // 93
        @SerializedName("sender_name")
        var senderName: String? // jack1 jack1
    ) : Parcelable
}