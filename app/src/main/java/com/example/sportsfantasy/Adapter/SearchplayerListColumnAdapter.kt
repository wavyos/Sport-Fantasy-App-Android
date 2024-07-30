package com.example.sportsfantasy.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsfantasy.Interface.PlayerListner
import com.example.sportsfantasy.Model.Player
import com.example.sportsfantasy.R
import de.hdodenhof.circleimageview.CircleImageView

class SearchplayerListColumnAdapter(private val context: Context, private val playerlist: ArrayList<Player>, val playerListner: PlayerListner
) : RecyclerView.Adapter<SearchplayerListColumnAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.row_player_column, parent, false)
        return MyViewHolder(itemView)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = playerlist[position]

        val FG: String = currentItem.FG
        val Ast: String = currentItem.Ast
        val BLK: String = currentItem.BLK
        val FGM: String = currentItem.FGM
        val FT: String = currentItem.FT
        val Reb: String = currentItem.Reb
        val STL: String = currentItem.STL
        val TOV: String = currentItem.TOV
        val fullname: String = currentItem.fullname!!
        val pid: String = currentItem.pid.toString()
        val positiontype: String = currentItem.positiontype
        val primaryposition: String = currentItem.primaryposition



        holder.tv_player_name.text = fullname

        holder.tv_positiontype.text = "$primaryposition,$positiontype"
        holder.tv_primaryposition.text = ""

        var salary: String = "0"
        salary = if (currentItem.salary == "-") {
            "0"
        } else {
            currentItem.salary.toString()
        }


        holder.civ_player.setOnClickListener {
            playerListner.onPlayerClick(position, currentItem)
        }

        holder.tv_player_name.setOnClickListener {
            playerListner.onPlayerClick(position, currentItem)
        }

        val list = playerlist.lastIndex

        if (position == list) {
            holder.view_line.visibility = View.GONE
        }


    }


    override fun getItemCount(): Int {
        return playerlist.size

    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tv_player_name: TextView = itemView.findViewById(R.id.tv_player_name)
        val tv_positiontype: TextView = itemView.findViewById(R.id.tv_positiontype)
        val tv_primaryposition: TextView = itemView.findViewById(R.id.tv_primaryposition)
        val tv_add: TextView = itemView.findViewById(R.id.tv_add)
        val ll_player: LinearLayout = itemView.findViewById(R.id.ll_player)
        val civ_player: CircleImageView = itemView.findViewById(R.id.civ_player)

        val view_line = itemView.findViewById<View>(R.id.view_line)

    }




}