package com.example.sportsfantasy.Adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsfantasy.Interface.ListItemClick
import com.example.sportsfantasy.Model.LeagueScoreCardResModel
import com.example.sportsfantasy.R
import com.example.sportsfantasy.databinding.RowLeagueMatchupBinding


open class LeagueMatchUpListAdapter(
    val context: Context,
    var list: ArrayList<LeagueScoreCardResModel.Matchup>,
    var itemClick: ListItemClick
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_league_matchup, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ViewHolder

        viewHolder.binding.tvTeamName.text = list[position].fisrtTeamName
        viewHolder.binding.tvTeamName1.text = list[position].secondTeamName

        viewHolder.binding.tvUsername.text = list[position].fisrtTeamUserName
        viewHolder.binding.tvUsername1.text = list[position].secondTeamUserName

        viewHolder.binding.tvLeaguePoint.text = list[position].fisrtTeamWinlose
        viewHolder.binding.tvLeaguePoint1.text = list[position].secondTeamWinlose

        viewHolder.binding.tvSalary.text = list[position].fisrtTeamWeeklyPoints.toString()
        viewHolder.binding.tvSalary1.text = list[position].secondTeamWeeklyPoints.toString()

        viewHolder.binding.ll.setOnClickListener {
            itemClick.onItemClick(position,"")
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: RowLeagueMatchupBinding = DataBindingUtil.bind(view)!!
    }
}