package com.example.sportsfantasy.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsfantasy.Interface.ListItemClick
import com.example.sportsfantasy.Model.StandingResModel
import com.example.sportsfantasy.R
import com.example.sportsfantasy.databinding.LeagueStandingPlayerBinding
import com.example.sportsfantasy.databinding.RowLeagueMatchupBinding
import com.example.sportsfantasy.databinding.RowPlayerPg2Binding

open class StandingsAdapter(
    val context: Context,
    var list: List<StandingResModel.StandingsData.EastTeam>,
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.league_standing_player,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ViewHolder
//        viewHolder.binding.gb.text = list[position].gb
//        viewHolder.binding.pa.text = list[position].pa
        viewHolder.binding.pf.text = list[position].pf
        viewHolder.binding.win.text = list[position].win!!.toBigDecimal().setScale(0, java.math.RoundingMode.UP).toDouble().toString()+"%"
        viewHolder.binding.playoff.text = list[position].playoff
        //viewHolder.binding.memberSide.text = list[position].memberSide
        viewHolder.binding.records.text = list[position].record
        viewHolder.binding.strk.text = list[position].strk
}
    override fun getItemCount(): Int {
        return list.size
    }
   inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: LeagueStandingPlayerBinding = DataBindingUtil.bind(view)!!
    }
}