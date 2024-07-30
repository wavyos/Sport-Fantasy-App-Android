package com.example.sportsfantasy.Model

import com.google.gson.annotations.SerializedName

data class GetMessageAddModel(
    val channel_id: Int,
    val league_id: Int,
    val sender_id: Int,
    val receiver_id: Int
)
