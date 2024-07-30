package com.example.sportsfantasy.Interface

import com.example.sportsfantasy.Model.UserLeagueResponse

interface Listner
{
    fun onDraftClickListner(position:Int, createdLeague: UserLeagueResponse.UserLeague)

    fun onInviteItemClickListner(position: Int, createdLeague: UserLeagueResponse.UserLeague)

    fun onTeamClickListner(position: Int, createdLeague: UserLeagueResponse.UserLeague)

    fun onMatchUpClickListner(position: Int, createdLeague: UserLeagueResponse.UserLeague)


}