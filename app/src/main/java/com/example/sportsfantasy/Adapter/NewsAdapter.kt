package com.example.sportsfantasy.Adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsfantasy.Fragment.MainFragment
import com.example.sportsfantasy.Interface.NewsItemClick
import com.example.sportsfantasy.R
import com.example.sportsfantasy.databinding.RowNewsLatestBinding

class NewsAdapter(val context: Context, var list: ArrayList<String>,val listener: NewsItemClick, ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_news_latest, parent, false)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ViewHolder
        viewHolder.binding.tvNewsTitle.text = list[position]
        viewHolder.binding.llNewsItem.setOnClickListener {
            listener.onNewsItemClick()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: RowNewsLatestBinding = DataBindingUtil.bind(view)!!
    }
}