package com.example.sportsfantasy.Model

data class CreateLeagueModel(
    val entry_fee: String,
    val is_entry_fee: String,
    val is_postseason: String,
    val league_name: String,
    val league_size: String,
    val league_type: String,
    val league_week: String,
    val tid: String,
    val user_id: String
)