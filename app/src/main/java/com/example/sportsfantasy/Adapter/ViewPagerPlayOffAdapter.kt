package com.example.sportsfantasy.Adapter

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.example.sportsfantasy.Model.PlayOff.Data
import com.example.sportsfantasy.R


class ViewPagerPlayOffAdapter(
    private val mContext: Context,
    private val itemList: ArrayList<Data>,
    private val height: Int
) : PagerAdapter() {
    private var layoutInflater: LayoutInflater? = null
    private lateinit var playoffAdapter : PlayOffAdapter
    private var MAXHEIGHT = 0;

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = LayoutInflater.from(mContext)
        val view =  layoutInflater!!.inflate(R.layout.row_main_play_off_item, container, false)
        val rvPlayOff = view.findViewById<RecyclerView>(R.id.rv_playoff_item)

        var lp: LinearLayout.LayoutParams? = null
        if(itemList.size == 1){
            lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        }
        /*val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        when(position){
            0 -> {
                MAXHEIGHT = 800
            }
            else -> {
                MAXHEIGHT -= 200
            }
        }
        lp.gravity = Gravity.CENTER*/

        val llm = LinearLayoutManager(mContext)
        if(lp != null){
            rvPlayOff.layoutParams = lp
        }
        rvPlayOff.layoutManager = llm
        playoffAdapter = PlayOffAdapter(mContext, itemList[position].groupList)
        rvPlayOff.adapter = playoffAdapter

        container.addView(view, position)
        return view
    }
    override fun getCount(): Int {
        return itemList.size
    }
    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view = `object` as View
        container.removeView(view)
    }
}
