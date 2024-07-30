package com.example.sportsfantasy.Model

data class LeageFieldResponse(
    val league_fees: List<Int>,
    val league_size: List<Int>,
    val league_weeks: List<Int>,
    val success: Boolean
)