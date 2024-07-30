package com.example.sportsfantasy.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsfantasy.Fragment.newFlow.SettingsFragment
import com.example.sportsfantasy.R


class MaxPlayerListAdapter(private val frag: SettingsFragment, private val items: ArrayList<String>) :
    RecyclerView.Adapter<MaxPlayerListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(frag.context).inflate(R.layout.raw_max_player_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvPlayerNo.text = "${position + 1}"
        holder.tvPlayerName.text = item

        holder.llMainItem.setOnClickListener {
            frag.showPlayerDialog(item)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val llMainItem: LinearLayout = itemView.findViewById(R.id.ll_max_player_item)
        val tvPlayerName: TextView = itemView.findViewById(R.id.tv_player_name)
        val tvPlayerNo: TextView = itemView.findViewById(R.id.tv_player_number)
    }
}
