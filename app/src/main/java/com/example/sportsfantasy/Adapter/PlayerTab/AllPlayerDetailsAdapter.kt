package com.example.sportsfantasy.Adapter.PlayerTab

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.TypefaceCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsfantasy.Model.Player
import com.example.sportsfantasy.Model.PlayerF.Data
import com.example.sportsfantasy.Model.PlayerF.PositionAll
import com.example.sportsfantasy.R
import java.math.RoundingMode

class AllPlayerDetailsAdapter(private val context: Context, private val playerData:ArrayList<Player>, private val isMain : Boolean) : RecyclerView.Adapter<AllPlayerDetailsAdapter.MyViewHolder>()
{
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val tv_position = itemView.findViewById<TextView>(R.id.tv_positiontype)
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

        val ll_main = itemView.findViewById<HorizontalScrollView>(R.id.scroll)
        val view_line = itemView.findViewById<View>(R.id.view_line)

    }

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
         val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_player_pg,parent,false)
         return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) 
    {
        try {
            val currentItem  = playerData[position]

            if(position % 2 == 0){
                holder.ll_main.setBackgroundColor(ContextCompat.getColor(context, R.color.row_even_color))
            } else {
                holder.ll_main.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            }

            var FG: String = currentItem.FG
            var Ast: String = currentItem.Ast
            var BLK: String = currentItem.BLK
            var FGM: String = currentItem.FGM
            var FT: String = currentItem.FT
            var Reb: String = currentItem.Reb
            var STL: String = currentItem.STL
            var TOV: String = currentItem.TOV
            var fullname: String = currentItem.fullname
            var pid: String = currentItem.pid.toString()
            var positiontype: String = currentItem.positiontype
            var primaryposition: String = currentItem.primaryposition

            holder.tv_position.isVisible = isMain
            holder.tv_position.text = primaryposition

            if(isMain){
                val typeface = ResourcesCompat.getFont(context, R.font.roboto_bold)
                holder.tv_points.typeface = TypefaceCompat.create(context,typeface, Typeface.BOLD)
            }


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

            // val isEnable =  currentItem.isEnable ?: false

            holder.tv_FGM.text = FGM
            holder.tv_PTS.text = FG
            holder.tv_REB.text = Reb
            holder.tv_FT.text = FT
            holder.tv_BLK.text = BLK
            holder.tv_AST.text = Ast
            holder.tv_STL.text = STL
            holder.tv_TOV.text = TOV


            var salary = "0"
            if (currentItem.salary.equals("-") || currentItem.salary.isEmpty() || currentItem.salary == "0")
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
            val list = playerData.lastIndex
            //Log.d("krunal",""+list)

            if (position==list)
            {
                //    holder.view_line.visibility = View.GONE
            }
        }catch (er:Exception){

        }
    }

     override fun getItemCount(): Int {
         return playerData.size
    }

}