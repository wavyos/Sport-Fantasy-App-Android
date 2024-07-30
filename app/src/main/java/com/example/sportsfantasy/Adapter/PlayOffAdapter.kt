package com.example.sportsfantasy.Adapter

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsfantasy.Model.PlayOff.Group
import com.example.sportsfantasy.R
import com.example.sportsfantasy.databinding.RowPlayOffDataLayoutBinding

open class PlayOffAdapter(
    private val context: Context,
    private val grouplist: List<Group>,
) : RecyclerView.Adapter<PlayOffAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_play_off_data_layout,parent,false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = grouplist[position]

        holder.binding.tvFirstTeamName.text = currentItem.fisrtTeamName
        holder.binding.tvFirstTeamSalary.text = "${currentItem.fisrtTeamSalary}"
        holder.binding.tvFirstTeamPoints.text = "${currentItem.fisrtTeamPoints}"

        holder.binding.tvSecondTeamName.text = currentItem.secondTeamName
        holder.binding.tvSecondTeamSalary.text = "${currentItem.secondTeamSalary}"
        holder.binding.tvSecondTeamPoints.text = "${currentItem.secondTeamPoints}"

    }

    override fun getItemCount(): Int {
        return grouplist.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
        var binding: RowPlayOffDataLayoutBinding = DataBindingUtil.bind(itemView)!!
    }

}