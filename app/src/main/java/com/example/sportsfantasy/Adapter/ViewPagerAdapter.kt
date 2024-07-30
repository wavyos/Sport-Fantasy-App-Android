package com.example.sportsfantasy.Adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.sportsfantasy.Activities.MainActivity
import com.example.sportsfantasy.Fragment.MainFragment
import com.example.sportsfantasy.R

class ViewPagerAdapter(private val context: MainFragment, private val images: List<Int>) :
    RecyclerView.Adapter<ViewPagerAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView_home_pager)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(context.context).inflate(R.layout.home_pager_item, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageResId = images[position]
        holder.imageView.setImageResource(imageResId)
        holder.imageView.setOnClickListener {
            context.setViewPagerClick(position)
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }
}