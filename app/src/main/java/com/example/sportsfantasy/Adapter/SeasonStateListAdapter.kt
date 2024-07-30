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
import com.example.sportsfantasy.Model.SeasonState
import com.example.sportsfantasy.R


class SeasonStateListAdapter(private val context: Context, private val seasonState: ArrayList<SeasonState>) : RecyclerView.Adapter<SeasonStateListAdapter.MyViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_season_stats,parent,false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        val CurrentItem  = seasonState[position]


        if(position%2 == 0){
            holder.main_view.setBackgroundColor(ContextCompat.getColor(context, R.color.main_activity_bg))
        }else{
            holder.main_view.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }

        holder.tv_salary.text = ""+CurrentItem.salary

        holder.tv_points.text = ""+"%.2f".format(CurrentItem.points.toFloat())

        if (CurrentItem.salaryPercentage == "-")
        {
            holder.tv_salary_percentage.setCompoundDrawables(null,null,null,null)
        }

        if (CurrentItem.year.length>4)
        {
            val currentString = CurrentItem.year
            val separated: List<String> = currentString.split("-")
            separated[0];
            separated[1];
            holder.tv_year.text = ""+separated[0]+"\n"+separated[1]

            Log.d("vr",""+separated[0])
            Log.d("vr",""+separated[1])
        }
        else
        {
            holder.tv_year.text = ""+CurrentItem.year
        }

        Log.d("vr",""+CurrentItem.year.length)

        holder.tv_salary_percentage.text = ""+CurrentItem.salaryPercentage
        holder.tv_fgm.text = ""+"%.2f".format(CurrentItem.FGM.toFloat())
        holder.tv_3pt.text = ""+"%.2f".format(CurrentItem.FG.toFloat())
        holder.tv_reb.text = ""+"%.2f".format(CurrentItem.Reb.toFloat())
        holder.tv_ft.text = ""+"%.2f".format(CurrentItem.FT.toFloat())
        holder.tv_blk.text = ""+"%.2f".format(CurrentItem.BLK.toFloat())
        holder.tv_stl.text = ""+"%.2f".format(CurrentItem.STL.toFloat())
        holder.tv_tov.text = ""+"%.2f".format(CurrentItem.TOV.toFloat())


        val list = seasonState.lastIndex
        Log.d("vr",""+list)

        if (position==list)
        {
            holder.view_line.visibility = View.GONE
        }


    }


    override fun getItemCount(): Int
    {
        return seasonState.size
    }

    class MyViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView)
    {
        val tv_year :TextView = itemView.findViewById(R.id.tv_year)
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
        val main_view :LinearLayout = itemView.findViewById(R.id.main_season_state_view)

    }


}