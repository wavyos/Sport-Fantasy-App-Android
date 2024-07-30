package com.example.sportsfantasy.Model

import com.google.gson.annotations.SerializedName

data class SendMessageAddModel(
    val channel_id: Int,
    val league_id: Int,
    val sender_id: Int,
    val receiver_id: Int,
    val message: String
)
