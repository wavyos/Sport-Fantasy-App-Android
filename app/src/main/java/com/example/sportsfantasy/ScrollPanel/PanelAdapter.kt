package com.sportsdb.scrollpanel

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class PanelAdapter {
    abstract val rowCount: Int
    abstract val columnCount: Int
    open fun getItemViewType(row: Int, column: Int): Int {
        return 0
    }

    abstract fun onBindViewHolder(holder: RecyclerView.ViewHolder, row: Int, column: Int)
    abstract fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
}