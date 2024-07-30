package com.example.sportsfantasy.ScrollPanel

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsfantasy.Activities.PgUserActivity
import com.example.sportsfantasy.Activities.SelectPlayerActivity
import com.example.sportsfantasy.Adapter.PlayerListAdapter
import com.example.sportsfantasy.Adapter.PlayerTab.cPlayerDetailsAdapter
import com.example.sportsfantasy.Model.Player
import com.example.sportsfantasy.R
import com.example.sportsfantasy.ScrollPanel.Model.ColumnData
import com.example.sportsfantasy.databinding.ItemCellBinding
import com.example.sportsfantasy.databinding.ItemColumnTitleBinding
import com.example.sportsfantasy.databinding.ItemPlayerBinding
import com.sportsdb.scrollpanel.PanelAdapter
import kotlin.coroutines.coroutineContext

class ScrollablePanelAdapter : PanelAdapter() {
    private var remainSalary = 0
    private var playersList = ArrayList<Player>()
    private var columnTitlesList = ArrayList<ColumnData>()
    private var TitlesList = ArrayList<ColumnData>()
    private var cellList = ArrayList<ArrayList<String?>>()

    private var listener: ((player: Player) -> Unit)? = null

    var allowAdd = true


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, row: Int, column: Int) {
        val viewType = getItemViewType(row, column)
        when (viewType) {
            COLUMN_TITLE_TYPE -> (holder as ColumnTitleHolder).bind(column)
            PLAYER_TYPE -> (holder as PlayerHolder).bind(row)
            CELL_TYPE -> (holder as CellHolder).bind(row, column)
            EMPTY_TYPE -> (holder as TitleViewHolder).bind(row)
            else -> (holder as CellHolder).bind(row, column)
        }

    }

    fun setOnItemClickListener(listener: (player: Player) -> Unit)
    {
        this.listener = listener
    }



    override val rowCount: Int
        get() = playersList.size + 1
    override val columnCount: Int
        get() = columnTitlesList.size

    override fun getItemViewType(row: Int, column: Int): Int {
        if (column == 0 && row == 0) {
            return EMPTY_TYPE
        }
        if (column == 0) {
            return PLAYER_TYPE
        }
        return if (row == 0) {
            COLUMN_TITLE_TYPE
        } else CELL_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            COLUMN_TITLE_TYPE -> return ColumnTitleHolder(
                ItemColumnTitleBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            PLAYER_TYPE -> return PlayerHolder(
                ItemPlayerBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            CELL_TYPE -> return CellHolder(
                ItemCellBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            EMPTY_TYPE -> return TitleViewHolder(
                ItemPlayerBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            else -> {
                return CellHolder(
                    ItemCellBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
        }
    }

    fun setPlayerList(playersList: ArrayList<Player>, remainSalary: Int)
    {
        this.playersList = playersList
        this.remainSalary = remainSalary
    }

    fun setColumnTitleList(columnTitlesList: ArrayList<ColumnData>, remainSalary: Int) {
        this.columnTitlesList = columnTitlesList
        this.remainSalary = remainSalary
    }

    fun setPlayerDetailsList(cellList: ArrayList<ArrayList<String?>>, remainSalary: Int) {
        this.cellList = cellList
        this.remainSalary = remainSalary


    }

    private inner class ColumnTitleHolder(private val binding: ItemColumnTitleBinding) :
        RecyclerView.ViewHolder(binding.root)
    {
        fun bind(position: Int)
        {
            binding.textViewColumnTitle.text = columnTitlesList[position].title
        }
    }

    private inner class PlayerHolder(private val binding: ItemPlayerBinding) :
        RecyclerView.ViewHolder(binding.root)
    {

        fun bind(position: Int) {

            val index = position - 1

            if(playersList.size > index)
            {
                if (playersList[index].fullname!!.length>10)
                {
                    binding.textViewPlayerName.textSize = 12F
                }
                if(playersList[index].fullname!!.contains("・")){
                    binding.textViewPlayerName.text = playersList[index].fullname!!.split("・")[1]
                } else {
                    binding.textViewPlayerName.text = playersList[index].fullname
                }
                binding.textViewPlayer1.text = playersList[index].positiontype
                binding.btnAdd.tag = index

                val isEnable =  playersList[index].isEnable ?: false

                binding.textViewPlayerName.setTextColor(if (isEnable) Color.BLACK else Color.BLACK)
                binding.textViewPlayer1.setTextColor(if (isEnable) Color.BLACK else Color.BLACK)
                binding.btnAdd.isClickable = isEnable


                if(playersList[index].salary.equals("-")){
                    allowAdd = false
                }else{
                    allowAdd = playersList[index].salary.toInt()<= remainSalary
                }


                if (allowAdd)
                {
                    binding.btnAdd.setTextColor(if (allowAdd) Color.BLACK else Color.BLACK)
                    binding.textViewPlayerName.setTextColor(if (allowAdd) Color.BLACK else Color.BLACK)
                    binding.textViewPlayer1.setTextColor(if (allowAdd) Color.BLACK else Color.BLACK)
                }
                else
                {
                    binding.textViewPlayerName.setTextColor(Color.BLACK)
                    binding.textViewPlayer1.setTextColor(Color.BLACK )

                }

                binding.btnAdd.setTextColor(if (allowAdd) Color.BLACK else Color.BLACK)
                binding.textViewPlayerName.setTextColor(if (allowAdd) Color.BLACK else Color.BLACK)


                if(allowAdd){
                    binding.btnAdd.isClickable = true
                    binding.btnAdd.isEnabled = true
                }else{
                    binding.btnAdd.isClickable = false
                    binding.btnAdd.isEnabled = false
                }

                binding.btnAdd.setOnClickListener {
                    if(playersList[index].salary.equals("-")){
                        allowAdd = false
                    }else{
                        allowAdd = playersList[index].salary.toInt()<= remainSalary
                    }
                    if (allowAdd)
                    {
                        val player = playersList[it.tag as Int]
                        listener?.invoke(player)
                    }
                }
                binding.textViewPlayerName.setOnClickListener {
                    val intent = Intent(binding.root.context, PgUserActivity::class.java)
                    intent.putExtra("pid",""+playersList[index].pid)
                    binding.root.context.startActivity(intent)
                }
            }
        }
    }

    private inner class CellHolder(private val binding: ItemCellBinding) :
        RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(row: Int, Cols: Int) {

            if (playersList.size >= row)
            {

                val index = row - 1
                binding.textViewCellData.text = cellList[index][Cols]

                val isEnable = playersList[index].isEnable?: false
                //Log.e("TAG", "bind: "+isEnable )

                if(playersList[index].salary.equals("-")){
                    allowAdd = false
                }else{
                    allowAdd = playersList[index].salary.toInt()<= remainSalary
                }
                if (allowAdd)
                {
                    binding.textViewCellData.setTextColor(if (isEnable) Color.BLACK else Color.BLACK)
                }
                else
                {
                    binding.textViewCellData.setTextColor(Color.BLACK)
                }
                binding.textViewCellData.setTextColor(if (allowAdd) Color.BLACK else Color.BLACK)
            }
        }
    }

    private class TitleViewHolder(private val binding: ItemPlayerBinding) :
        RecyclerView.ViewHolder(
            binding.root
        ) {
            fun bind(row:Int) {
                binding.textViewPlayerTitle.visibility = View.VISIBLE
                binding.textViewPlayerTitle.text = binding.root.context.getString(R.string.player_c)
                binding.textViewPlayerName.visibility = View.INVISIBLE
                binding.imageViewIcon.visibility = View.INVISIBLE
                binding.textViewPlayer1.visibility = View.INVISIBLE

                binding.tvSeprator.visibility = if (row==0) View.GONE else View.VISIBLE

            }
        }

    companion object {
        private const val PLAYER_TYPE = 0
        private const val COLUMN_TITLE_TYPE = 1
        private const val CELL_TYPE = 2
        private const val EMPTY_TYPE = 4
    }


}