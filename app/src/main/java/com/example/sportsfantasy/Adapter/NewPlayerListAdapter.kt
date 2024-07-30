package com.example.sportsfantasy.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sportsfantasy.Activities.ClickPickListener
import com.example.sportsfantasy.Model.Player
import com.example.sportsfantasy.R
import de.hdodenhof.circleimageview.CircleImageView
import java.text.DecimalFormat

class NewPlayerListAdapter(
    private val context: Context,
    private val playerList: ArrayList<Player>,
    val remainSalary: Int,
    val listener: ClickPickListener,
    val p_id: String
) : RecyclerView.Adapter<NewPlayerListAdapter.MyViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.raw_new_player_list_item,parent,false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        try {
            if(position == 0){
                holder.llTitleView.visibility = View.VISIBLE
            } else {
                holder.llTitleView.visibility = View.GONE
            }

            Glide.with(context)
                .load(playerList[position].headshot)
                .placeholder(ContextCompat.getDrawable(context, R.drawable.player_placeholder_img))
                .into(holder.player_image)

            if(playerList[position].fullname == null){
                holder.tvPlayerName.text = "No Name"
            } else {
                holder.tvPlayerName.text = playerList[position].fullname
            }
            holder.tvPlayerSalary.text = playerList[position].salary
            val formater = DecimalFormat("#.##")
            val tempPoints =  playerList[position].salary_percentage.toDouble()
            holder.tvPlayerPPG.text = playerList[position].salary_percentage
            holder.tvPlayerPoints.text = String.format("%.2f", playerList[position].points.toFloat())

            if(p_id != "0"){
                if(playerList[position].pid == p_id.toInt()){
                    holder.tvPlayerPickUnpick.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context,R.color.bg))
                    holder.tvPlayerPickUnpick.text = "\u2713"
                    holder.tvPlayerPickUnpick.setTextColor(ContextCompat.getColor(context,R.color.white))
                } else {
                    holder.tvPlayerPickUnpick.text = ""
                    holder.tvPlayerPickUnpick.backgroundTintList = null
                }
            }
            holder.tvPlayerPickUnpick.setOnClickListener {
                if(remainSalary < playerList[position].salary.toInt()){
                    Toast.makeText(context, "Can not select this player", Toast.LENGTH_SHORT).show()
                } else {
                    listener.pickUnpickClick(playerList[position], true)
                }
            }

        }catch (er:Exception){

        }
    }


    override fun getItemCount(): Int
    {
        return playerList.size
    }

    fun updateData(filteredItems: ArrayList<Player>) {
        playerList.clear()
        playerList.addAll(filteredItems)
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPlayerName : TextView = itemView.findViewById(R.id.tv_player_name)
        val tvPlayerSalary : TextView = itemView.findViewById(R.id.tv_player_salary)
        val tvPlayerPPG : TextView = itemView.findViewById(R.id.tv_player_ppg)
        val tvPlayerPoints : TextView = itemView.findViewById(R.id.tv_player_points)
        val tvPlayerPickUnpick : TextView = itemView.findViewById(R.id.tv_pick_unpick_player)
        val llTitleView : LinearLayout = itemView.findViewById(R.id.ll_title_view)
        val player_image : CircleImageView = itemView.findViewById(R.id.civ_player_image)
    }


}