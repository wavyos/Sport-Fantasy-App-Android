package com.example.sportsfantasy.Adapter.MatchUpA

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsfantasy.Fragment.T_MatchupFragment
import com.example.sportsfantasy.Model.MatchUp.Data
import com.example.sportsfantasy.Model.MatchUp.TeamPlayer
import com.example.sportsfantasy.R
import kotlinx.coroutines.NonDisposableHandle
import kotlinx.coroutines.NonDisposableHandle.parent

class teamPlayerAdapter(private val context: Context, val teamList: ArrayList<TeamPlayer>) :
    RecyclerView.Adapter<teamPlayerAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_first_team_playername =
            itemView.findViewById<TextView>(R.id.tv_first_team_playername)
        val tv_fp_point = itemView.findViewById<TextView>(R.id.tv_fp_point)

        val tv_positiontype = itemView.findViewById<TextView>(R.id.tv_positiontype)

        val tv_second_team_playername =
            itemView.findViewById<TextView>(R.id.tv_second_team_playername)
        val tv_sp_point = itemView.findViewById<TextView>(R.id.tv_sp_point)

        val llTotalData = itemView.findViewById<LinearLayout>(R.id.ll_total_data)
        val tvTotalFpPoint = itemView.findViewById<TextView>(R.id.tv_total_fp_point)
        val tvTotalSpPoint = itemView.findViewById<TextView>(R.id.tv_total_sp_point)
        val viewLineTotal = itemView.findViewById<View>(R.id.view_line_daily_data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.row_team_players, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.llTotalData.visibility = View.GONE
        val currentPlayerData = teamList[position]
        val firstTeamPlayerName = currentPlayerData.firstplayer_fullname
        val secondTeamPlayerName = currentPlayerData.secondplayer_fullname
        val firstTeamPlayerPoint = currentPlayerData.firstplayer_game_points
        val secondTeamPlayerPoint = currentPlayerData.secondplayer_game_points
        val positionType = currentPlayerData.positiontype

        if ((firstTeamPlayerName == "-") || (firstTeamPlayerName == "")) {
            holder.tv_first_team_playername.text = "Empty"
        } else {
            holder.tv_first_team_playername.text = firstTeamPlayerName
        }

        if ((secondTeamPlayerName == "-") || (secondTeamPlayerName == "")) {
            holder.tv_second_team_playername.text = "Empty"
        } else {
            holder.tv_second_team_playername.text = secondTeamPlayerName
        }

        if ((firstTeamPlayerPoint == "-") || (firstTeamPlayerPoint == "")) {
            holder.tv_fp_point.text = "0.0"
            holder.tv_fp_point.visibility = View.GONE
        } else {
            holder.tv_fp_point.text = firstTeamPlayerPoint
        }

        if ((secondTeamPlayerPoint == "-") || (secondTeamPlayerPoint == "")) {
            holder.tv_sp_point.text = "0.0"
            holder.tv_sp_point.visibility = View.GONE
        } else {
            holder.tv_sp_point.text = secondTeamPlayerPoint
        }

        if ((positionType == "-") || (positionType == "")) {
            holder.tv_positiontype.text = "-"
        } else {
            holder.tv_positiontype.text = positionType
        }
        val customTypeface: Typeface = ResourcesCompat.getFont(context, R.font.roboto_bold)!!
        if (position == (teamList.size -1)) {
            holder.llTotalData.visibility = View.VISIBLE
            var fpTotal = 0.0
            var spTotal = 0.0
            var i = 0
            while (i < teamList.size) {
                fpTotal += teamList[i].firstplayer_game_points.toFloat()
                spTotal += teamList[i].secondplayer_game_points.toFloat()
                i++
            }

            holder.tvTotalFpPoint.text = "$fpTotal"
            holder.tvTotalSpPoint.text = "$spTotal"

            holder.tvTotalSpPoint.textSize = 14F
            holder.tvTotalFpPoint.textSize = 14F

            holder.tvTotalFpPoint.typeface = customTypeface
            holder.tvTotalSpPoint.typeface = customTypeface
        }
        holder.tv_fp_point.typeface = customTypeface
        holder.tv_sp_point.typeface = customTypeface
    }

    override fun getItemCount(): Int {
        return teamList.size
    }

}
