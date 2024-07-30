package com.example.sportsfantasy.Adapter.LeagueTeam

import android.annotation.SuppressLint
import android.content.Context
import android.service.autofill.UserData
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsfantasy.DataStorage.SharedPrefManager
import com.example.sportsfantasy.Interface.leagueTeamListner
import com.example.sportsfantasy.Model.LeagueTeam.Team
import com.example.sportsfantasy.R

class LeagueTeamAdapter(private val context: Context, private val arrTeamList: ArrayList<Team>,
                        val admin:String,
                        val leagueListner:leagueTeamListner) : RecyclerView.Adapter<LeagueTeamAdapter.MyViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_team_edit,parent,false)


        return MyViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        val CurrentItem  = arrTeamList[position]
        val teamId = position + 1
        val teamName = CurrentItem.team_name
        val division = CurrentItem.division
        val manger = CurrentItem.is_manager
        val status = CurrentItem.status

        if( admin == "${CurrentItem.user_id}"){
            holder.ll.visibility = View.GONE
            holder.iv_remove_manager.setImageDrawable(context.getDrawable(R.drawable.tick))
        }
        else {
            holder.ll.visibility = View.VISIBLE
            holder.iv_remove_manager.setImageDrawable(context.getDrawable(R.drawable._ic_close_))
        }
        if (teamName != null || division!= null || manger!=null || status !=null)
        {
            holder.tv_teamId.text = ""+teamId
            holder.tv_team_name.text = if(teamName=="") "" else teamName
            holder.tv_division.text = if (division=="") "" else division
            holder.tv_status.text = if (status=="") "" else status
        }

        holder.btn_makeManger.setOnClickListener {
            leagueListner.makeManager(position,arrTeamList[position])
        }
        
        holder.btn_removeUser.setOnClickListener { 
            leagueListner.removeUser(position,arrTeamList[position])
        }

    }

    override fun getItemCount(): Int {
        return arrTeamList.size
    }

    class MyViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView)
    {
        val tv_teamId :TextView = itemView.findViewById(R.id.tv_teamId)
        val tv_team_name :TextView = itemView.findViewById(R.id.tv_team_name)
        val tv_division :TextView = itemView.findViewById(R.id.tv_division)
        val iv_remove_manager :ImageView = itemView.findViewById(R.id.iv_remove)
        val tv_status:TextView = itemView.findViewById(R.id.tv_status)
        val btn_makeManger:TextView = itemView.findViewById(R.id.btn_makeManger)
        val btn_removeUser:TextView = itemView.findViewById(R.id.btn_removeUser)
        val view:View = itemView.findViewById(R.id.view)
        val ll:LinearLayout = itemView.findViewById(R.id.ll_button)

    }
}