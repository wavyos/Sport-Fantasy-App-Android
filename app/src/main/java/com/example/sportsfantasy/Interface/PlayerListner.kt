package com.example.sportsfantasy.Interface

import com.example.sportsfantasy.Model.Player
import com.example.sportsfantasy.Model.Position
import com.example.sportsfantasy.Model.UserLeague

interface PlayerListner
{
    fun onPlayerClick(position:Int, player: Player)

    fun OnAddPlayerClick(position: Int,player: Player)


}