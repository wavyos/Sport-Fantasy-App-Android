package com.example.sportsfantasy.Adapter.Matchup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsfantasy.Model.Matchup_Test.starNameModel
import com.example.sportsfantasy.R

class starCustomAdapter(private val mList: List<starNameModel>) : RecyclerView.Adapter<starCustomAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_matchup, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]

        // sets the text to the textview from our itemHolder class
        holder.tv_name.text = ItemsViewModel.text
        holder.tv_name2.text = ItemsViewModel.text2

        if (position==0)
        {
            holder.tv_name.textSize = 24F
        }
        else
        {
            holder.tv_name.textSize = 16F
        }

        if (ItemsViewModel.text2 == "")
        {
            holder.tv_name2.visibility = View.GONE
        }
        else
        {
            holder.tv_name2.visibility = View.VISIBLE
        }

        if (ItemsViewModel.text == "")
        {
            holder.tv_name.visibility = View.GONE
        }
        else
        {
            holder.tv_name.visibility = View.VISIBLE
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tv_name: TextView = itemView.findViewById(R.id.tv_name)
        val tv_name2: TextView = itemView.findViewById(R.id.tv_name2)
    }
}
