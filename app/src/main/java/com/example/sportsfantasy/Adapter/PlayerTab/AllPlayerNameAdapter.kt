package com.example.sportsfantasy.Adapter.PlayerTab

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sportsfantasy.Activities.BottomNavigationActivity
import com.example.sportsfantasy.Activities.PgUserActivity
import com.example.sportsfantasy.Fragment.newFlow.PgUserFragment
import com.example.sportsfantasy.Fragment.newFlow.SelectPlayerFragment
import com.example.sportsfantasy.Model.Player
import com.example.sportsfantasy.Model.PlayerF.PositionAll
import com.example.sportsfantasy.R
import de.hdodenhof.circleimageview.CircleImageView

class AllPlayerNameAdapter(private val context: Context, private val playerAllData:ArrayList<Player>, private val isMain:Boolean) : RecyclerView.Adapter<AllPlayerNameAdapter.MyViewHolder>()
{
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {

        val tv_player_name: TextView = itemView.findViewById(R.id.tv_player_name)
        val tv_index_no: TextView = itemView.findViewById(R.id.tv_index_no)
        val tv_positiontype: TextView = itemView.findViewById(R.id.tv_positiontype)
      //  val tv_primaryposition: TextView = itemView.findViewById(R.id.tv_primaryposition)
        val ll_player: LinearLayout = itemView.findViewById(R.id.ll_player)
        val ll_main: LinearLayout = itemView.findViewById(R.id.ll)
        val civ_player: CircleImageView = itemView.findViewById(R.id.civ_player)

        val view_line = itemView.findViewById<View>(R.id.view_line)

    }

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
     {
         val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_player_tab, parent, false)
         return MyViewHolder(itemView)
     }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        try {
            val currentItem = playerAllData[position]

            if(position % 2 == 0){
                holder.ll_main.setBackgroundColor(ContextCompat.getColor(context, R.color.row_even_color))
            } else {
                holder.ll_main.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            }

            val fullname: String = currentItem.fullname

//            Log.d("username", ""+currentItem.fullname)

            val positiontype: String = currentItem.positiontype
            val primaryposition: String = currentItem.primaryposition

            holder.tv_player_name.text = fullname

            holder.tv_positiontype.isVisible = !isMain

            holder.tv_index_no.text = "${position+1}"

            holder.tv_positiontype.text = "$primaryposition, $positiontype"
            //holder.tv_primaryposition.text = ""

            var salary: String = "0"
            if (currentItem.salary == "-" || currentItem.salary== "0")
            {
                salary = "0"
            }
            else
            {
                salary = currentItem.salary.toString()
            }

            val pid:String = currentItem.pid.toString()

            holder.tv_player_name.setOnClickListener {
//                Toast.makeText(context, context.getString(R.string.str_work_in_progress),Toast.LENGTH_SHORT).show()
                try {
                    if (context is BottomNavigationActivity) {
                        val bundle = Bundle()
                        bundle.putString("pid",pid)
                        bundle.putString("headshot", currentItem.headshot.toString())

                        val fragment = PgUserFragment()
                        fragment.arguments = bundle

                        context.addFragment(fragment)
                    }
                }catch (er:Exception){

                }

                /*val playerScreen = Intent(context, PgUserActivity::class.java)
                playerScreen.putExtra("pid",pid)
                playerScreen.putExtra("headshot", currentItem.headshot.toString())
                context.startActivity(playerScreen)*/
            }
            Glide.with(context)
                .load(currentItem.headshot)
                .placeholder(ContextCompat.getDrawable(context, R.drawable.player_placeholder_img))
                .into(holder.civ_player)


            holder.civ_player.setOnClickListener {
//                Toast.makeText(context, context.getString(R.string.str_work_in_progress),Toast.LENGTH_SHORT).show()

                try {
                    if (context is BottomNavigationActivity) {
                        val bundle = Bundle()
                        bundle.putString("pid",pid)
                        bundle.putString("headshot", currentItem.headshot.toString())

                        val fragment = PgUserFragment()
                        fragment.arguments = bundle

                        context.addFragment(fragment)
                    }
                }catch (er:Exception){

                }

               /* val playerScreen = Intent(context, PgUserActivity::class.java)
                playerScreen.putExtra("pid",pid)
                context.startActivity(playerScreen)*/
            }



            val list = playerAllData.lastIndex


            if (position == list)
            {
                // holder.view_line.visibility = View.GONE
            }
        }catch (er:Exception){
            Log.d("getAllPlayerAPi2: ", er.message.toString())
        }


    }

    override fun getItemCount(): Int {
        return playerAllData.size
    }

}