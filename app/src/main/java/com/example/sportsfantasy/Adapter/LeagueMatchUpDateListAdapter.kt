package com.example.sportsfantasy.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsfantasy.Interface.ListItemClick
import com.example.sportsfantasy.Model.LeagueScoreCardResModel
import com.example.sportsfantasy.R
import com.example.sportsfantasy.databinding.ListLeagueMatchupBinding
import java.util.*

class LeagueMatchUpDateListAdapter(
    private val context: Context?,
    private var weekList: ArrayList<LeagueScoreCardResModel.Week>,
    private val onItemClick: ListItemClick
) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_league_matchup, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ViewHolder

        viewHolder.binding.txtMatchUpDate.text = weekList[position].value

        if(weekList[position].isSelected){
            viewHolder.binding.imgChecked.visibility = View.VISIBLE
        }else{
            weekList[position].isSelected = false
            viewHolder.binding.imgChecked.visibility = View.INVISIBLE
        }

        viewHolder.binding.llMain.setOnClickListener {
            weekList[position].isSelected = !weekList[position].isSelected
            notifyDataSetChanged()
            onItemClick.onItemClick(position, "")
        }
    }

    override fun getItemCount(): Int {
        return weekList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: ListLeagueMatchupBinding = DataBindingUtil.bind(view)!!
    }
}
