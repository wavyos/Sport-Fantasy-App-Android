package com.example.sportsfantasy.Adapter

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsfantasy.Model.Rank
import com.example.sportsfantasy.R
import com.example.sportsfantasy.databinding.RowBtmshtRankDataItemBinding

class RankAdapter(private val applicationContext: Context,private val ranklist: ArrayList<Rank>) :
    RecyclerView.Adapter<RankAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.row_btmsht_rank_data_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = ranklist[position]

        when (currentItem.rank!! % 10) {
            1 -> holder.binding.tvRankNumber.text = "${currentItem.rank}" + Html.fromHtml("<sup>st</sup>")
            2 -> holder.binding.tvRankNumber.text = "${currentItem.rank}" + Html.fromHtml("<sup>nd</sup>")
            3 -> holder.binding.tvRankNumber.text = "${currentItem.rank}" + Html.fromHtml("<sup>rd</sup>")
            else -> holder.binding.tvRankNumber.text = "${currentItem.rank}" + Html.fromHtml("<sup>th</sup>")
        }
        holder.binding.tvRankerName.text = currentItem.username ?: "Unknown"
        holder.binding.tvWinLose.text = "${currentItem.total} - ${currentItem.totalmatch!! - currentItem.total!!}"
    }

    override fun getItemCount(): Int {
        return ranklist.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: RowBtmshtRankDataItemBinding = DataBindingUtil.bind(this.itemView)!!
    }
}