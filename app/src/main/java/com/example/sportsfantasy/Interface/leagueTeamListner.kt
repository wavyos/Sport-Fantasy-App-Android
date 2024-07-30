package com.example.sportsfantasy.Interface

import com.example.sportsfantasy.Model.LeagueTeam.Team

interface leagueTeamListner {
    fun removeManager(position:Int, team: Team)

    fun removeUser(position: Int,team: Team)

    fun makeManager(position: Int,team: Team)
}