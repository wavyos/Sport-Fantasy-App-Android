package com.example.sportsfantasy.Model.UpdateLeague

data class UpdatedLeague(
    val created_at: String,
    val deleted_at: String,
    val entry_fee: Int,
    val id: Int,
    val invitation_code: String,
    val invitation_link: String,
    val is_entry_fee: String,
    val is_postseason: String,
    val league_name: String,
    val league_size: Int,
    val league_type: String,
    val league_week: Int,
    val tid: Int,
    val updated_at: String,
    val user_id: Int
)