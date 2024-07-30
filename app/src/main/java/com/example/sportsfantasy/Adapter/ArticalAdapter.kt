package com.example.sportsfantasy.Adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsfantasy.Model.ArticalData
import com.example.sportsfantasy.R
import com.example.sportsfantasy.databinding.RowArticalItemLayoutBinding
import com.example.sportsfantasy.databinding.RowNewsLatestBinding

class ArticalAdapter(val context: Context, var list: ArrayList<ArticalData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_artical_item_layout, parent, false)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ViewHolder
        viewHolder.binding.imgPlayer.setImageDrawable(list[position].img)
        viewHolder.binding.tvArticalTitle.text = list[position].title
        viewHolder.binding.tvArticalSubtitle.text = list[position].sub_title
        viewHolder.binding.tvArticalDesc.text = list[position].description
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: RowArticalItemLayoutBinding = DataBindingUtil.bind(view)!!
    }
}