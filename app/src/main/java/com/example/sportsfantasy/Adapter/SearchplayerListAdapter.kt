package com.example.sportsfantasy.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsfantasy.Interface.PlayerListner
import com.example.sportsfantasy.Model.Player
import com.example.sportsfantasy.R
import java.math.RoundingMode


class SearchplayerListAdapter(private val context: Context, private val arrSearchPlayerList: ArrayList<Player>, val playerListner: PlayerListner) : RecyclerView.Adapter<SearchplayerListAdapter.MyViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_player_pg,parent,false)
        return MyViewHolder(itemView)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        val currentItem  = arrSearchPlayerList[position]

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


        holder.tv_FGM.text = FGM
        holder.tv_PTS.text = FG
        holder.tv_REB.text = Reb
        holder.tv_FT.text = FT
        holder.tv_BLK.text = BLK
        holder.tv_AST.text = Ast
        holder.tv_STL.text = STL
        holder.tv_TOV.text = TOV


        var salary: String = "0"
        if (currentItem.salary.equals("-"))
        {
            salary = "0"
            holder.tv_salary.text = "$"+salary
        }
        else
        {
            salary = currentItem.salary.toString()
            holder.tv_salary.text = "$"+salary
        }




        val list = arrSearchPlayerList.lastIndex
        //Log.d("krunal",""+list)

        if (position==list)
        {
            holder.view_line.visibility = View.GONE
        }


    }


    override fun getItemCount(): Int {
        return arrSearchPlayerList.size
    }

    class MyViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView)
    {

        val ll_player_details :LinearLayout = itemView.findViewById(R.id.ll_player_details)
        val scroll :HorizontalScrollView = itemView.findViewById(R.id.scroll)

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