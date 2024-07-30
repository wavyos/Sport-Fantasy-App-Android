package com.example.sportsfantasy.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsfantasy.Model.StandingResModel
import com.example.sportsfantasy.R
import com.example.sportsfantasy.databinding.LeagueStandingPlayerNameBinding
import com.example.sportsfantasy.databinding.LeagueStandingWestPlayerNameBinding

open class StandingWestPlayerAdapter(val context: Context,
                                var list: List<StandingResModel.StandingsData.WestTeam>,
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.league_standing_west_player_name,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ViewHolder
        viewHolder.binding.tvWestPlayerName.text = list[position].teamName
        viewHolder.binding.tvWestAdd.text = list[position].teamUserName
    }
   inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: LeagueStandingWestPlayerNameBinding = DataBindingUtil.bind(view)!!
    }
}