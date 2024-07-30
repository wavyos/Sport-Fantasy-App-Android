package com.example.sportsfantasy.Model

data class UserLeague(
    val created_at: String,
    val deleted_at: String,
    val entry_fee: String,
    val id: String,
    val invitation_code: String,
    val invitation_link: String,
    val is_entry_fee: String,
    val is_league_admin: String,
    val is_postseason: String,
    val league_name: String,
    val league_size: String,
    val league_type: String,
    val league_week: String,
    val league_start_date: String,
    val remaining_members: String,
    val tid: String,
    val updated_at: String,
    val user_id: String? = null)

//data class MatchupDetails(
//
//    val fisrt_team_name: String,
//    val first_member_name: String,
//    val fisrt_team_points: String,
//    val fisrt_team_winlose: String,
//    val second_team_name: String,
//    val second_member_name: String,
//    val second_team_points: String,
//    val second_team_winlose: String
//)

