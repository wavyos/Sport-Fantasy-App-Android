package com.example.sportsfantasy.Adapter

import android.content.Context
import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsfantasy.Model.PlayOff.PlayOffResponse
import com.example.sportsfantasy.R
import com.example.sportsfantasy.databinding.RowMainPlayOffItemBinding

open class PlayOffMainAdapter(
    private val context: Context,
    private val grouplist: PlayOffResponse
) : RecyclerView.Adapter<PlayOffMainAdapter.ViewHolder>() {

    private lateinit var playoffAdapter : PlayOffAdapter



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_main_play_off_item,parent,false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        for(tempItem in grouplist.playOffData){
            val mainLinLayout = LinearLayout(context)
            mainLinLayout.orientation = LinearLayout.VERTICAL
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            mainLinLayout.layoutParams = lp

            val roundTextParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            roundTextParams.gravity = Gravity.CENTER_HORIZONTAL
            roundTextParams.setMargins(40,-20,40,20)

            val tv = TextView(context)
            tv.text = tempItem.rountTitle
            tv.textSize = 14F
            tv.textAlignment = View.TEXT_ALIGNMENT_CENTER
            tv.setPadding(10,30,10,20)
            tv.background = context.resources.getDrawable(R.drawable.custom_card)
            tv.setTypeface(tv.typeface, Typeface.BOLD)
            mainLinLayout.addView(tv, roundTextParams)

            val rv = RecyclerView(context)
            val params = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.MATCH_PARENT
            )
            rv.background = null
            rv.setPadding(0,10,0,0)
            rv.layoutParams = params

            val llm = LinearLayoutManager(context)
            playoffAdapter = PlayOffAdapter(context, tempItem.groupList)
            rv.adapter = playoffAdapter
            rv.layoutManager = llm
            rv.visibility = View.VISIBLE
            mainLinLayout.addView(rv)

            holder.binding.llPlayOffMain.addView(mainLinLayout)
        }
        
    }

    override fun getItemCount(): Int {
        return 1
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
        var binding: RowMainPlayOffItemBinding = DataBindingUtil.bind(itemView)!!
    }

}