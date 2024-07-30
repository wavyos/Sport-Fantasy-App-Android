package com.example.sportsfantasy.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsfantasy.Interface.PlayerListner
import com.example.sportsfantasy.Model.Player
import com.example.sportsfantasy.Model.PlayerProfile
import com.example.sportsfantasy.R
import de.hdodenhof.circleimageview.CircleImageView


class ProfileAdapter(private val context: Context, private val playerProfile: ArrayList<PlayerProfile>) : RecyclerView.Adapter<ProfileAdapter.MyViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_player_pg,parent,false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        val CurrentItem  = playerProfile[position]

        val name:String = CurrentItem.fullname

        holder.tv_player_name.text = name


    }


    override fun getItemCount(): Int {
        return playerProfile.size
    }

    class MyViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView)
    {
        val tv_player_name :TextView = itemView.findViewById(R.id.tv_player_name)
        val ll_player :LinearLayout = itemView.findViewById(R.id.ll_player)
        val ll_player_details :LinearLayout = itemView.findViewById(R.id.ll_player_details)
        val scroll :HorizontalScrollView = itemView.findViewById(R.id.scroll)
        val civ_player :CircleImageView = itemView.findViewById(R.id.civ_player)

    }


}