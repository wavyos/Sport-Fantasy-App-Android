package com.example.sportsfantasy.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.opengl.Visibility
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sportsfantasy.Activities.MainActivity
import com.example.sportsfantasy.DataStorage.SharedPrefManager
import com.example.sportsfantasy.Interface.Listner
import com.example.sportsfantasy.Model.Rank
import com.example.sportsfantasy.Model.UserComLeagues
import com.example.sportsfantasy.Model.UserLeagueResponse
import com.example.sportsfantasy.R
import com.example.sportsfantasy.databinding.RowComLeagueListBinding
import com.example.sportsfantasy.databinding.RowLeagueListBinding
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.GZIPOutputStream
import kotlin.collections.ArrayList

open class LeagueListAdapter(
    private val context: Activity,
    private val leaguelist: ArrayList<UserLeagueResponse.UserLeague>,
    val listner: Listner
) : RecyclerView.Adapter<LeagueListAdapter.ViewHolder>() {

    var onItemClick: ((List<Rank>) -> Unit)? = null
    lateinit var viewHolder: ViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_league_list, parent, false)
        viewHolder = ViewHolder(itemView)
        return viewHolder
    }

    public fun String.capitalizeWords(): String = split(" ").map { it.capitalize() }.joinToString(" ")

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        try {
            val currentItem = leaguelist[position]

            var isLeagueStarted = false

            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val strDate = sdf.parse(currentItem.leagueStartDate)
            // val strDate = sdf.parse("2023-01-02 00:00:00")
            isLeagueStarted = System.currentTimeMillis() > strDate.time


            if (currentItem.totalmatch != 0 && currentItem.totalmatch != null) {
                try {
                    holder.binding.cvDraftLeauge.isVisible = false
                    holder.binding.cvUpComingLeauge.isVisible = true
                    val login_user_id = SharedPrefManager.getInstance(context).getLogin.userDetails.id
                    var isWin = currentItem.win_team_user_id == login_user_id

                    if (currentItem.win_team_user_id != 0) {

//                        holder.binding.llCardFooter.isVisible = false
                        holder.binding.cvDailyDraftLeave.isVisible = true
                        holder.binding.cvDailyDraftPost.isVisible = false

                        if(isWin){
                            Glide.with(context)
                                .load(ContextCompat.getDrawable(context, R.drawable.ic_trophy))
                                .into(holder.binding.imgWinLoss)
                            holder.binding.currentDrweeek.text = context.getString(R.string.str_finished_first)
                        }else{
                            Glide.with(context)
                                .load(ContextCompat.getDrawable(context, R.drawable.icn_loss))
                                .into(holder.binding.imgWinLoss)
                            holder.binding.currentDrweeek.text = context.getString(R.string.str_loose_the_league)
                        }
                        holder.binding.leagueDrname.text = "" + currentItem.leagueName?.capitalizeWords()
                        holder.binding.tvTeamNameFirst.text = currentItem.leagueName?.capitalizeWords()
                        holder.binding.tvWin.text = "${currentItem.total}"
                        val fi = currentItem.totalmatch!! - currentItem.total!!
                        holder.binding.tvLose.text = "$fi"
                        holder.binding.tvPlayerNameFirst.text = currentItem.username?.capitalizeWords()

                        holder.binding.btnFirstMyTeam.setOnClickListener {
                            listner.onTeamClickListner(position, currentItem)
                        }

                    } else {
                        if(isWin){
                            Glide.with(context)
                                .load(ContextCompat.getDrawable(context, R.drawable.ic_trophy))
                                .into(holder.binding.imgWinLoss)
                            holder.binding.currentDrweeek.text = context.getString(R.string.str_finished_first)
                        }else{
                            Glide.with(context)
                                .load(ContextCompat.getDrawable(context, R.drawable.icn_loss))
                                .into(holder.binding.imgWinLoss)
                            holder.binding.currentDrweeek.text = context.getString(R.string.str_loose_the_league)
                        }
                        holder.binding.cvDailyDraftPost.isVisible = true
                        holder.binding.cvDailyDraftLeave.isVisible = false
                    }
                    holder.binding.btnRank.setOnClickListener() {
                        onItemClick?.invoke(currentItem.rank as List<Rank>)
                    }
                } catch (ex: Exception) {
                    Log.d("Error : ", "" + ex.localizedMessage)
                }
            } else {
//                holder.binding.llCardFooter.isVisible = false
                holder.binding.cvDailyDraftLeave.isVisible = false
                holder.binding.cvDailyDraftPost.isVisible = true

                if (currentItem.draftActive == 1) {
//                    holder.binding.llCardFooter.isVisible = false

                    holder.binding.leagueDrname.text = "" + currentItem.leagueName?.capitalizeWords()

                    val isadmin = currentItem.isLeagueAdmin
                    val remainmember = currentItem.remainingMembers

                    holder.binding.currentDrweeek.text =
                        context.resources.getString(R.string.week) + currentItem.currentWeek.toString()+context.getString(R.string.week_last_text)

                    if (!currentItem.matchupDetails?.isEmpty()!!) {
                        holder.binding.cvDraftLeauge.isVisible = false
                        val matchupDetails = currentItem.matchupDetails?.get(0)

                        val fisrtTeamName = matchupDetails?.fisrtTeamName ?: ""
                        val firstMemberName = matchupDetails?.firstMemberName ?: ""
                        val fisrtTeamPoints = matchupDetails?.fisrtTeamPoints ?: ""
                        val fisrtTeamWinlose = matchupDetails?.fisrtTeamWinlose ?: ""

                        val secondTeamName = matchupDetails?.secondTeamName ?: ""
                        val secondMemberName = matchupDetails?.secondMemberName ?: ""
                        val secondTeamPoints = matchupDetails?.secondTeamPoints ?: ""
                        val secondTeamWinlose = matchupDetails?.secondTeamWinlose ?: ""

                        holder.binding.tvDrftName.text = capitalize(fisrtTeamName)
                        holder.binding.tvDrftPlayerName.text =
                            "${capitalize(firstMemberName)} | $fisrtTeamWinlose"
                        holder.binding.tcDrftScore.text = fisrtTeamPoints

                        holder.binding.tvDrstName.text = capitalize(secondTeamName)
                        holder.binding.tvDrstPlayerName.text =
                            "${capitalize(secondMemberName)} | $secondTeamWinlose"
                        holder.binding.tvDrstScore.text = secondTeamPoints

                        holder.binding.btnDrmatchup.setOnClickListener {
//                            listner.onMatchUpClickListner(position, currentItem)
                            listner.onTeamClickListner(position, currentItem)
                        }
                        holder.binding.btnDrdraft.setOnClickListener {
                            listner.onDraftClickListner(position, currentItem)
                        }

                        /*holder.binding.tvDrteam.setOnClickListener {
                            listner.onTeamClickListner(position, currentItem)
                        }*/
                    } else {
//                        holder.binding.llCardFooter.isVisible = false
                        if (currentItem.remainingMembers!! > 0 || !isLeagueStarted) { // Draft

                            holder.binding.cvDraftLeauge.isVisible = true
                            holder.binding.cvUpComingLeauge.isVisible = false

                            holder.binding.tvLeagueName.text = "" + currentItem.leagueName?.capitalizeWords()

                            if (isadmin.equals("0")) {

                                holder.binding.inviteMember.visibility = View.VISIBLE
                                holder.binding.inviteMember.isVisible = true
                                holder.binding.tvMessage.text = ""
                                holder.binding.tvDraft.text = context.getString(R.string.draft)

                            } else {

                                if (remainmember == 0) {
                                    holder.binding.inviteMember.visibility = View.VISIBLE
                                    holder.binding.inviteMember.isVisible = true
                                    holder.binding.tvMessage.text = "All position have been filled in the league"
                                    holder.binding.tvDraft.text = context.getString(R.string.draft)
                                } else {
                                    holder.binding.tvDraft.text = context.getString(R.string.invite_member)
                                    holder.binding.inviteMember.visibility = View.VISIBLE
                                    holder.binding.inviteMember.isVisible = true
                                    holder.binding.tvMessage.text = context.getString(R.string.str_member_required_draft_one) + " $remainmember " + context.getString(
                                            R.string.str_member_required_draft_two
                                        )
                                }

                            }

                            holder.binding.tvDraft.setOnClickListener {
                                if (isadmin.equals("0")) {
                                    listner.onDraftClickListner(position, currentItem)
                                }else {

                                if (remainmember == 0) {
                                    listner.onDraftClickListner(position, currentItem)
                                }else{
                                    listner.onInviteItemClickListner(position, currentItem)
                                }
                                }
                            }

                            holder.binding.tvTeam.setOnClickListener {
                                listner.onTeamClickListner(position, currentItem)
                            }

                            holder.binding.inviteMember.setOnClickListener {
                                listner.onInviteItemClickListner(position, currentItem)
                            }
                        } else {
                            holder.binding.cvDraftLeauge.isVisible = false
                            holder.binding.cvUpComingLeauge.isVisible = true
                        }
                    }

                } else {
                    if (currentItem.remainingMembers!! > 0 || !isLeagueStarted) { // Draft

                        holder.binding.cvDraftLeauge.isVisible = true
                        holder.binding.cvUpComingLeauge.isVisible = false

                        holder.binding.tvLeagueName.text = "" + currentItem.leagueName?.capitalizeWords()

                        val isadmin = currentItem.isLeagueAdmin
                        val remainmember = currentItem.remainingMembers

                        if (isadmin.equals("0")) {

                            holder.binding.inviteMember.visibility = View.VISIBLE
                            holder.binding.inviteMember.isVisible = true
                            holder.binding.tvMessage.text = ""
                            holder.binding.tvDraft.text = context.getString(R.string.draft)

                        } else {

                            if (remainmember == 0) {
                                holder.binding.inviteMember.visibility = View.VISIBLE
                                holder.binding.inviteMember.isVisible = true
                                holder.binding.tvMessage.text =
                                    "All position have been filled in the league"
                                holder.binding.tvDraft.text = context.getString(R.string.draft)

                            } else {
                                holder.binding.inviteMember.visibility = View.VISIBLE
                                holder.binding.inviteMember.isVisible = true
                                holder.binding.tvMessage.text =
                                    context.getString(R.string.str_member_required_draft_one) + " $remainmember " + context.getString(
                                        R.string.str_member_required_draft_two
                                    )
                                holder.binding.tvDraft.text = context.getString(R.string.invite_member)
                            }

                        }

                        holder.binding.tvDraft.setOnClickListener {
                            if (isadmin.equals("0")) {
                                listner.onDraftClickListner(position, currentItem)
                            } else {
                                if (remainmember == 0) {
                                    listner.onDraftClickListner(position, currentItem)
                                }else{
                                    listner.onInviteItemClickListner(position, currentItem)
                                }
                            }
                        }

                        holder.binding.tvTeam.setOnClickListener {
                            listner.onTeamClickListner(position, currentItem)
                        }

                        holder.binding.inviteMember.setOnClickListener {
                            listner.onInviteItemClickListner(position, currentItem)
                        }
                    } else { // UpComing List

                        holder.binding.cvDraftLeauge.isVisible = false
                        holder.binding.cvUpComingLeauge.isVisible = true

                        holder.binding.leagueName.text = currentItem.leagueName!!.capitalizeWords()
                        holder.binding.currentWeeek.text = currentItem.currentWeek.toString()+context.getString(R.string.week_last_text)

                        if (!currentItem.matchupDetails?.isEmpty()!!) {

                            val matchupDetails = currentItem.matchupDetails?.get(0)

                            val fisrtTeamName = matchupDetails?.fisrtTeamName ?: ""
                            val firstMemberName = matchupDetails?.firstMemberName ?: ""
                            val fisrtTeamPoints = matchupDetails?.fisrtTeamPoints ?: ""
                            val fisrtTeamWinlose = matchupDetails?.fisrtTeamWinlose ?: ""

                            val secondTeamName = matchupDetails?.secondTeamName ?: ""
                            val secondMemberName = matchupDetails?.secondMemberName ?: ""
                            val secondTeamPoints = matchupDetails?.secondTeamPoints ?: ""
                            val secondTeamWinlose = matchupDetails?.secondTeamWinlose ?: ""

                            holder.binding.tvFtName.text = capitalize(fisrtTeamName)
                            holder.binding.tvFtPlayerName.text =
                                "${capitalize(firstMemberName)} | $fisrtTeamWinlose"
                            holder.binding.tcFtScore.text = fisrtTeamPoints

                            holder.binding.tvStName.text = capitalize(secondTeamName)
                            holder.binding.tvStPlayerName.text =
                                "${capitalize(secondMemberName)} | $secondTeamWinlose"
                            holder.binding.tvStScore.text = secondTeamPoints

                            holder.binding.btnMyteam.setOnClickListener {
                                listner.onTeamClickListner(position, currentItem)
                            }
                            holder.binding.btnMatchup.setOnClickListener {
                                listner.onMatchUpClickListner(position, currentItem)
                            }
                        }
                    }
                }
            }
        }catch (er:Exception){

        }

    }

    fun capitalize(str: String): String? {
        return str.trim().split("\\s+".toRegex())
            .joinToString(" ") { it.capitalize() }
    }

    override fun getItemCount(): Int {
        return leaguelist.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: RowLeagueListBinding = DataBindingUtil.bind(itemView)!!
    }
}