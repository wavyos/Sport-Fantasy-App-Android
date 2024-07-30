package com.example.sportsfantasy.Model.MatchUp

data class Data(
    val fisrt_team_name: String,
    val fisrt_team_points: String,
    val fisrt_team_salary: String,
    val fisrt_team_id: Int,
    val second_team_name: String,
    val second_team_points: String,
    val second_team_salary: String,
    val second_team_id: Int,
    val team_players: List<TeamPlayer>
)