package com.example.sportsfantasy.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsfantasy.Interface.ILoadMore
import com.example.sportsfantasy.Interface.PlayerListner
import com.example.sportsfantasy.Model.Player
import com.example.sportsfantasy.R
import java.math.RoundingMode
import java.util.*

class PlayerListAdapter(private val context: Context, private val playerlist: MutableList<Player>, val playerListner: PlayerListner? = null) : RecyclerView.Adapter<PlayerListAdapter.MyViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_player_pg,parent,false)

        return MyViewHolder(itemView)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun onBindViewHolder(holder:MyViewHolder, position: Int)
    {

            val currentItem  = playerlist[position]

            var FG: String = currentItem.FG
            var Ast: String = currentItem.Ast
            var BLK: String = currentItem.BLK
            var FGM: String = currentItem.FGM
            var FT: String = currentItem.FT
            var Reb: String = currentItem.Reb
            var STL: String = currentItem.STL
            var TOV: String = currentItem.TOV
            var fullname: String = currentItem.fullname!!
            var pid: String = currentItem.pid.toString()
            var positiontype: String = currentItem.positiontype
            var primaryposition: String = currentItem.primaryposition

//        var float2 : Float = str2.toFloat();

            if((FG == "-") || (Ast == "-") || (BLK == "-") || (FGM == "-") || (FT == "-") || (Reb=="-") || (STL=="-") || (TOV == "-"))
            {
                FG = "0"
                Ast = "0"
                BLK = "0"
                FGM = "0"
                FT = "0"
                Reb = "0"
                STL = "0"
                TOV = "0"
            }
            else
            {
                FGM = FGM.toBigDecimal().setScale(0, RoundingMode.UP).toDouble().toString()
                FG = FG.toBigDecimal().setScale(0, RoundingMode.UP).toDouble().toString()
                Reb = Reb.toBigDecimal().setScale(0, RoundingMode.UP).toDouble().toString()
                FT = FT.toBigDecimal().setScale(0, RoundingMode.UP).toDouble().toString()
                BLK = BLK.toBigDecimal().setScale(0, RoundingMode.UP).toDouble().toString()
                Ast = Ast.toBigDecimal().setScale(0, RoundingMode.UP).toDouble().toString()
                STL = STL.toBigDecimal().setScale(0, RoundingMode.UP).toDouble().toString()
                TOV = TOV.toBigDecimal().setScale(0, RoundingMode.UP).toDouble().toString()


            }

            val isEnable =  currentItem.isEnable ?: false

            holder.tv_FGM.text = FGM
            holder.tv_PTS.text = FG
            holder.tv_REB.text = Reb
            holder.tv_FT.text = FT
            holder.tv_BLK.text = BLK
            holder.tv_AST.text = Ast
            holder.tv_STL.text = STL
            holder.tv_TOV.text = TOV

            holder.tv_salary.setTextColor(if (isEnable) Color.BLACK else Color.LTGRAY)
            holder.tv_FGM.setTextColor(if (isEnable) Color.BLACK else Color.LTGRAY)
            holder.tv_PTS.setTextColor(if (isEnable) Color.BLACK else Color.LTGRAY)
            holder.tv_REB.setTextColor(if (isEnable) Color.BLACK else Color.LTGRAY)
            holder.tv_FT.setTextColor(if (isEnable) Color.BLACK else Color.LTGRAY)
            holder.tv_BLK.setTextColor(if (isEnable) Color.BLACK else Color.LTGRAY)
            holder.tv_AST.setTextColor(if (isEnable) Color.BLACK else Color.LTGRAY)
            holder.tv_STL.setTextColor(if (isEnable) Color.BLACK else Color.LTGRAY)
            holder.tv_TOV.setTextColor(if (isEnable) Color.BLACK else Color.LTGRAY)



            var salary = "0"
            if (currentItem.salary.equals("-") || currentItem.salary.isEmpty())
            {
                holder.tv_salary.text = "$"+salary
            }
            else
            {
                salary = currentItem.salary
                holder.tv_salary.text = "$"+salary
            }

        try {
            holder.tv_points.text = "%.2f".format(currentItem.points.toBigDecimal().toDouble())
        }catch (er: java.lang.Exception){
            holder.tv_points.text = "0.0"
        }
            val list = playerlist.lastIndex


            if (position==list)
            {
                holder.view_line.visibility = View.GONE
            }



    }


    override fun getItemCount(): Int
    {
        return playerlist.size
    }

    class MyViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView)
    {

        val tv_points = itemView.findViewById<TextView>(R.id.tv_points)
        val tv_salary = itemView.findViewById<TextView>(R.id.tv_salary)
        val tv_FGM = itemView.findViewById<TextView>(R.id.tv_FGM)
        val tv_PTS = itemView.findViewById<TextView>(R.id.tv_PTS)
        val tv_REB = itemView.findViewById<TextView>(R.id.tv_REB)
        val tv_FT = itemView.findViewById<TextView>(R.id.tv_FT)
        val tv_AST = itemView.findViewById<TextView>(R.id.tv_AST)
        val tv_BLK = itemView.findViewById<TextView>(R.id.tv_BLK)
        val tv_STL = itemView.findViewById<TextView>(R.id.tv_STL)
        val tv_TOV = itemView.findViewById<TextView>(R.id.tv_TOV)

        val view_line = itemView.findViewById<View>(R.id.view_line)

    }


}