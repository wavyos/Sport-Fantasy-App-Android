package com.example.sportsfantasy.Model

data class SearchPlayerListResponse(
    val playerList: List<Player>,
    val success: Boolean,
    val total_items: Int,
    val total_pages: Int
)