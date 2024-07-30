package com.example.sportsfantasy.Model

data class PlayerListResponse(
    val playerList: List<Player>,
    val success: Boolean,
    val total_items: Int,
    val total_pages: Int
)