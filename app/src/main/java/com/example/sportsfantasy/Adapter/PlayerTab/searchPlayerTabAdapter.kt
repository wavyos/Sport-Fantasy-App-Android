package com.example.sportsfantasy.Adapter.PlayerTab

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsfantasy.Activities.PgUserActivity
import com.example.sportsfantasy.Model.PlayerF.PositionAll
import com.example.sportsfantasy.Model.PlayerF.PositionC
import com.example.sportsfantasy.Model.PlayerF.PositionPF
import com.example.sportsfantasy.Model.PlayerF.PositionPG
import com.example.sportsfantasy.Model.SearchTab.Data
import com.example.sportsfantasy.R
import de.hdodenhof.circleimageview.CircleImageView

class searchPlayerTabAdapter(private val context: Context, private val searchTabPlayerList:ArrayList<Data>) : RecyclerView.Adapter<searchPlayerTabAdapter.MyViewHolder>()
{
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) 
    {

        val tv_player_name: TextView = itemView.findViewById(R.id.tv_player_name)
        val tv_positiontype: TextView = itemView.findViewById(R.id.tv_positiontype)

        val view_line = itemView.findViewById<View>(R.id.view_line)

    }

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder 
     {
         val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_search_player_tab, parent, false)
         return MyViewHolder(itemView) 
     }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        val currentItem = searchTabPlayerList[position]
        holder.tv_positiontype.visibility = View.VISIBLE

        val fullname: String = currentItem.fullname
        val positiontype: String = currentItem.positiontype
        val primaryposition: String = currentItem.primaryposition

        holder.tv_player_name.text = fullname

        holder.tv_positiontype.text = "$primaryposition, $positiontype"

        var salary: String = "0"


        val pid:String = currentItem.pid

        holder.itemView.setOnClickListener{
            val playerScreen = Intent(context, PgUserActivity::class.java)
            playerScreen.putExtra("pid",pid)
            context.startActivity(playerScreen)
        }


        val list = searchTabPlayerList.lastIndex


        if (position == list)
        {
           // holder.view_line.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return searchTabPlayerList.size
    }

}