package com.example.sportsfantasy.Model.PlayerF

import com.example.sportsfantasy.Model.Player

data class Data(
    val position_All: List<Player>,
    val position_C: List<Player>,
    val position_PF: List<Player>,
    val position_PG: List<Player>,
    val position_SF: List<Player>,
    val position_SG: List<Player>
)