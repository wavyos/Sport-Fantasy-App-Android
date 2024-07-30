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
import com.example.sportsfantasy.R
import de.hdodenhof.circleimageview.CircleImageView

class pgPlayerNameAdapter(private val context: Context, private val playerAllData:ArrayList<PositionPG>) : RecyclerView.Adapter<pgPlayerNameAdapter.MyViewHolder>()
{
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) 
    {

        val tv_player_name: TextView = itemView.findViewById(R.id.tv_player_name)
        val tv_positiontype: TextView = itemView.findViewById(R.id.tv_positiontype)
        val tv_index_no: TextView = itemView.findViewById(R.id.tv_index_no)
        val ll_player: LinearLayout = itemView.findViewById(R.id.ll_player)
        val civ_player: CircleImageView = itemView.findViewById(R.id.civ_player)
        
        val view_line = itemView.findViewById<View>(R.id.view_line)

    }

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder 
     {
         val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_player_tab, parent, false)
         return MyViewHolder(itemView) 
     }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        val currentItem = playerAllData[position]
        holder.tv_positiontype.visibility = View.VISIBLE

        val fullname: String = currentItem.fullname
        val positiontype: String = currentItem.positiontype
        val primaryposition: String = currentItem.primaryposition

        holder.tv_player_name.text = fullname
        holder.tv_index_no.text = "${position + 1}"
        holder.tv_positiontype.text = "$primaryposition, $positiontype"

        var salary: String = "0"
        if (currentItem.salary == "-" || currentItem.salary== "0")
        {
            salary = "0"
        }
        else
        {
            salary = currentItem.salary.toString()
        }

        val pid:String = currentItem.pid

        holder.tv_player_name.setOnClickListener {
            val playerScreen = Intent(context, PgUserActivity::class.java)
            playerScreen.putExtra("pid",pid)
            context.startActivity(playerScreen)
        }

        holder.civ_player.setOnClickListener {
            val playerScreen = Intent(context, PgUserActivity::class.java)
            playerScreen.putExtra("pid",pid)
            context.startActivity(playerScreen)
        }


        val list = playerAllData.lastIndex


        if (position == list)
        {
           // holder.view_line.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return playerAllData.size
    }

}