package com.example.sportsfantasy.Model

data class PlayerProfileResponse(
    val game_log: List<GameLog>,
    val player_profile: PlayerProfile,
    val season_state: List<SeasonState>,
    val success: Boolean
)