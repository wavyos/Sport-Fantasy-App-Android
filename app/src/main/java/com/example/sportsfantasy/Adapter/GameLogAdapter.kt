package com.example.sportsfantasy.Adapter

import android.R.attr.data
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsfantasy.Model.GameLog
import com.example.sportsfantasy.Model.SeasonState
import com.example.sportsfantasy.R


class GameLogAdapter(private val context: Context, private val gameLogList:ArrayList<GameLog>) : RecyclerView.Adapter<GameLogAdapter.MyViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_game_log,parent,false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        val CurrentItem  = gameLogList[position]


        if(position % 2 == 0){
            holder.main_view.setBackgroundColor(ContextCompat.getColor(context,R.color.main_activity_bg))
        } else {
            holder.main_view.setBackgroundColor(ContextCompat.getColor(context,R.color.white))
        }

        holder.tv_date.text = ""+CurrentItem.date
        holder.tv_salary.text = ""+CurrentItem.salary
        try {
            holder.tv_points.text = "%.2f".format(CurrentItem.points.toBigDecimal().toDouble())
        }catch (er: java.lang.Exception){
            holder.tv_points.text = "0.0"
        }

        if (CurrentItem.salaryPercentage == "-")
        {
            holder.tv_salary_percentage.setCompoundDrawables(null,null,null,null)
        }

        holder.tv_salary_percentage.text = ""+CurrentItem.salaryPercentage
        holder.tv_fgm.text = ""+CurrentItem.FGM
        holder.tv_3pt.text = ""+CurrentItem.FG
        holder.tv_reb.text = ""+CurrentItem.Reb
        holder.tv_ft.text = ""+CurrentItem.FT
        holder.tv_blk.text = ""+CurrentItem.BLK
        holder.tv_stl.text = ""+CurrentItem.STL
        holder.tv_tov.text = ""+CurrentItem.TOV


        val list = gameLogList.lastIndex


        if (position==list)
        {
            holder.view_line.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int
    {
        return gameLogList.size
    }

    class MyViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView)
    {
        val tv_date :TextView = itemView.findViewById(R.id.tv_date)
        val tv_salary :TextView = itemView.findViewById(R.id.tv_salary)
        val tv_points :TextView = itemView.findViewById(R.id.tv_points)
        val tv_salary_percentage :TextView = itemView.findViewById(R.id.tv_salary_percentage)
        val tv_fgm :TextView = itemView.findViewById(R.id.tv_fgm)
        val tv_3pt :TextView = itemView.findViewById(R.id.tv_3pt)
        val tv_reb :TextView = itemView.findViewById(R.id.tv_reb)
        val tv_ft :TextView = itemView.findViewById(R.id.tv_ft)
        val tv_blk :TextView = itemView.findViewById(R.id.tv_blk)
        val tv_stl :TextView = itemView.findViewById(R.id.tv_stl)
        val tv_tov :TextView = itemView.findViewById(R.id.tv_tov)

        val view_line :View = itemView.findViewById(R.id.view_line)
        val main_view :LinearLayout = itemView.findViewById(R.id.main_rv_gamelog_item)

    }


}