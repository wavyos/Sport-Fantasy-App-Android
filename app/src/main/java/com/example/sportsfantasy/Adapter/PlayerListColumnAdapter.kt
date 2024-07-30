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
import kotlin.collections.ArrayList

class PlayerListColumnAdapter(private val context: Context, private val arrSearchPlayerList: MutableList<Player>,
                              val playerListner: PlayerListner) : RecyclerView.Adapter<PlayerListColumnAdapter.MyViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_player_column, parent, false)
        return MyViewHolder(itemView)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = arrSearchPlayerList[position]


        val fullname: String = currentItem.fullname!!
        val positiontype: String = currentItem.positiontype
        val primaryposition: String = currentItem.primaryposition

        holder.tv_player_name.text = fullname

        holder.tv_positiontype.text = "$primaryposition,$positiontype"
        holder.tv_primaryposition.text = ""

        var salary: String = "0"
        if (currentItem.salary.equals("-"))
        {
            salary = "0"
        }
        else
        {
            salary = currentItem.salary.toString()
        }

        val isEnable =  currentItem.isEnable ?: false

        holder.tv_add.setTextColor(if (isEnable) Color.BLACK else Color.LTGRAY)
        holder.tv_player_name.setTextColor(if (isEnable) Color.BLACK else Color.LTGRAY)
        holder.tv_add.isClickable = isEnable

        holder.civ_player.setOnClickListener {
            playerListner!!.onPlayerClick(position, currentItem)
        }

        holder.tv_player_name.setOnClickListener {
            playerListner?.onPlayerClick(position, currentItem)
        }

        holder.tv_add.setOnClickListener {
            if(isEnable){
                playerListner?.OnAddPlayerClick(position, currentItem)
            }
        }

        val list = arrSearchPlayerList.lastIndex



        if (position == list) {
            holder.view_line.visibility = View.GONE
        }


    }


    override fun getItemCount(): Int {

        return arrSearchPlayerList.size

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