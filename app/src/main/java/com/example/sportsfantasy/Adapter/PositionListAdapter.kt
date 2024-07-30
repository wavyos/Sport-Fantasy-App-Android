package com.example.sportsfantasy.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsfantasy.Interface.PositionClickListner
import com.example.sportsfantasy.Model.Position
import com.example.sportsfantasy.R
import de.hdodenhof.circleimageview.CircleImageView

class PositionListAdapter(private val context: Context, private val position: ArrayList<Position>, val PClistner: PositionClickListner) : RecyclerView.Adapter<PositionListAdapter.MyViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_position_list_new,parent,false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, ps: Int)
    {

        val currentItem  = position[ps]



//        holder.tv_select_pg.text = currentItem.code

        var small = 0;
        var start = 0;
        var starter = 0;

        val codename:String = ""+currentItem.code;
        val styledText = "<b><font color='#6C63FF'>$codename</font></b>"

        when(codename){
            "C"->{
//                binding.tvLeagueName.text = getString(R.string.select_c)
                holder.tv_select_pg.setText(Html.fromHtml(context.getString(R.string.select_c)), TextView.BufferType.SPANNABLE)
            }
            "PF"->{
//                binding.tvLeagueName.text = getString(R.string.select_pf)
                holder.tv_select_pg.setText(Html.fromHtml(context.getString(R.string.select_pf)), TextView.BufferType.SPANNABLE)
            }
            "PG"->{
//                binding.tvLeagueName.text = getString(R.string.select_pg)
                holder.tv_select_pg.setText(Html.fromHtml(context.getString(R.string.select_pg)), TextView.BufferType.SPANNABLE)
            }
            "SF"->{
//                binding.tvLeagueName.text = getString(R.string.select_sf)
                holder.tv_select_pg.setText(Html.fromHtml(context.getString(R.string.select_sf)), TextView.BufferType.SPANNABLE)
            }
            "SG"->{
//                binding.tvLeagueName.text = getString(R.string.select_sg)
                holder.tv_select_pg.setText(Html.fromHtml(context.getString(R.string.select_sg)), TextView.BufferType.SPANNABLE)
            }
            else->{
//                binding.tvLeagueName.text = getString(R.string.select) + "ALL"
                holder.tv_select_pg.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE)
            }
        }

//        holder.tv_select_pg.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE)

        holder.tv_select_pg.setOnClickListener{
            PClistner.onPositionClick(ps,currentItem)
        }

//        holder.ic_remove_player.setOnClickListener {
//            PClistner.onPositionDraftRemove(ps,currentItem)
//        }

        holder.tv_player_name.setOnClickListener {
            PClistner.onPlayerOpen(ps,currentItem)
        }

        holder.civ_player_image.setOnClickListener{
            PClistner.onPlayerOpen(ps,currentItem)
        }

        holder.tv_player_add_remove.setOnClickListener {
            if (currentItem.player.size == 0) {
                PClistner.onPlayerAdd(ps, currentItem)
            }else{
                PClistner.onPlayerEdit(ps, currentItem)
            }
        }


       if (currentItem.player.size == 0)
        {
            holder.tv_player_add_remove.text = context.getString(R.string.add)
//            holder.rl_player.visibility = View.GONE
            holder.tv_player_add_remove.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context,R.color.bg))
            holder.tv_player_add_remove.setTextColor(ContextCompat.getColor(context,R.color.white))
            holder.tv_player_name.text = "No player selected"
        }
        else
        {
            val fullname = currentItem.player[0].fullname
            //val matach_date = CurrentItem.player[0].match_date
            var salary = currentItem.player[0].salary
            val salary_percentage = currentItem.player[0].salary_percentage
            if (fullname!=null)
            {
                holder.tv_player_name.text = ""+fullname
            } else{
                holder.tv_player_name.text = "No Name"
            }

            Log.d("position list", "onBindViewHolder: salary$salary")



            var ssalary=0;

            if (salary != null)
            {
                if (salary.equals("0") || salary.equals("-") || salary.equals("null") || salary.isNullOrEmpty())
                {
                    salary = "0"
                    holder.tv_salary.text = "$"+ 0
                }
                else
                {
                    ssalary = salary.toInt()

                    if (ssalary<15)
                    {
                        small = small + 1

                    }

                    if (ssalary>=15 && ssalary<=20)
                    {
                        starter = starter + 1

                    }

                    if (ssalary>20)
                    {
                        start = start + 1

                    }

                    holder.tv_salary.text = "$"+ ssalary


                }


            }
            else
            {
                holder.tv_salary.text = "$"+ ssalary
            }

            if (salary_percentage != null)
            {

                if (salary_percentage == "0")
                {
                    holder.tv_salary_percentage.text = ""+0+"%"
                }
                else
                {
                    holder.tv_salary_percentage.text = ""+salary_percentage+"%"
                }
            }
            else
            {
                holder.tv_salary_percentage.text = ""+0+"%"
            }
            holder.rl_player.visibility = View.VISIBLE
            holder.tv_select_pg.isClickable = false
            holder.tv_select_pg.setCompoundDrawables(null,null,null,null)

        }


    }


    override fun getItemCount(): Int
    {
        return position.size
    }

    class MyViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView)
    {
        val tv_select_pg :TextView = itemView.findViewById(R.id.tv_select_pg)
        val tv_player_name :TextView = itemView.findViewById(R.id.tv_player_name)
//        val tv_match_date :TextView = itemView.findViewById(R.id.tv_match_date)
//        val ic_remove_player :CircleImageView = itemView.findViewById(R.id.ic_remove_player)
        val tv_salary :TextView = itemView.findViewById(R.id.tv_salary)
        val tv_salary_percentage :TextView = itemView.findViewById(R.id.tv_salary_percentage)
        val rl_player :RelativeLayout = itemView.findViewById(R.id.rl_player)
        val civ_player_image :CircleImageView = itemView.findViewById(R.id.civ_player_image)
        val tv_player_add_remove :TextView = itemView.findViewById(R.id.tv_edit_or_remove_player)


    }


}