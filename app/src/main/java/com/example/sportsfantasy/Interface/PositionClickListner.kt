package com.example.sportsfantasy.Interface

import com.example.sportsfantasy.Model.Position
import com.example.sportsfantasy.Model.UserLeague

interface PositionClickListner
{
    fun onPositionClick(position:Int, positionMain: Position)

    fun onPositionDraftRemove(position:Int, positionMain: Position)

    fun onPlayerOpen(position:Int, positionMain: Position)

    fun onPlayerAdd(position:Int, positionMain: Position)

    fun onPlayerEdit(position:Int, positionMain: Position)
}