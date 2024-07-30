package com.example.sportsfantasy.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsfantasy.Model.StandingResModel
import com.example.sportsfantasy.R
import com.example.sportsfantasy.databinding.LeagueStandingPlayerBinding
import com.example.sportsfantasy.databinding.LeagueWestStandingPlayerDetailsBinding

@Suppress("INFERRED_TYPE_VARIABLE_INTO_EMPTY_INTERSECTION_WARNING")
class StandingWestAdapter(val context: Context,
                          var list: List<StandingResModel.StandingsData.WestTeam>,
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.league_west_standing_player_details,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ViewHolder
//        viewHolder.binding.westGb.text = list[position].gb
//        viewHolder.binding.westPa.text = list[position].pa
        viewHolder.binding.westPf.text = list[position].pf
        viewHolder.binding.westWin.text =  list[position].win!!.toBigDecimal().setScale(0, java.math.RoundingMode.UP).toDouble().toString()+"%"
        viewHolder.binding.westPlayoff.text = list[position].playoff
        //viewHolder.binding.westMemberSide.text = list[position].memberSide
        viewHolder.binding.westRecords.text = list[position].record
        viewHolder.binding.westStrk.text = list[position].strk
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: LeagueWestStandingPlayerDetailsBinding = DataBindingUtil.bind(view)!!
    }
}