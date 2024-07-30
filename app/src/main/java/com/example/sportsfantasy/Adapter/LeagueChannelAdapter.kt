package com.example.sportsfantasy.Adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsfantasy.Interface.ListItemClick
import com.example.sportsfantasy.Model.GenerateChannelListResModel
import com.example.sportsfantasy.Model.LeagueScoreCardResModel
import com.example.sportsfantasy.R
import com.example.sportsfantasy.databinding.RowLeagueMatchupBinding
import com.example.sportsfantasy.databinding.RowMessagesBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


open class LeagueChannelAdapter(
    val context: Context,
    val userID: String,
    var list: ArrayList<GenerateChannelListResModel.Conv>,
    var itemClick: ListItemClick
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_messages, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ViewHolder

        viewHolder.binding.tvTeamName.text = list[position].title
        ///viewHolder.binding.tvTime.text = list[position].lastMessageTime

        if(list[position].lastSenderName == userID){
            if(!list[position].lastMessage.isNullOrEmpty()){
                viewHolder.binding.txtMessage.text = "You" + " : " + list[position].lastMessage
            } else {
                viewHolder.binding.txtMessage.text = context.getString(R.string.str_no_message)
            }
        }else{
            if(!list[position].lastMessage.isNullOrEmpty()){
                viewHolder.binding.txtMessage.text = list[position].lastSenderName + " : " + list[position].lastMessage
            } else {
                viewHolder.binding.txtMessage.text = context.getString(R.string.str_no_message)
            }
        }


        if (!list[position].lastMessageTime.isNullOrEmpty()) {
            val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            df.timeZone = TimeZone.getTimeZone("UTC")
            val date = df.parse(list[position].lastMessageTime!!)
            df.timeZone = TimeZone.getDefault()
            val formattedDate = df.format(date)

            val calendar = Calendar.getInstance()
            calendar.time = date

            calendar.set(Calendar.HOUR_OF_DAY,0)
            calendar.set(Calendar.MINUTE,0)
            calendar.set(Calendar.SECOND,0)
            calendar.set(Calendar.MILLISECOND,0)

            val calendar1 = Calendar.getInstance()

            calendar1.set(Calendar.HOUR_OF_DAY,0)
            calendar1.set(Calendar.MINUTE,0)
            calendar1.set(Calendar.SECOND,0)
            calendar1.set(Calendar.MILLISECOND,0)

            if(calendar.timeInMillis == calendar1.timeInMillis){

                val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
                df.timeZone = TimeZone.getTimeZone("UTC")
                val date = df.parse(list[position].lastMessageTime!!)
                df.timeZone = TimeZone.getDefault()
                val formattedDate = df.format(date)

                val originalFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val targetFormat = SimpleDateFormat("HH:mm")
                val date1: Date
                try {
                    date1 = originalFormat.parse(formattedDate)
                    viewHolder.binding.tvTime.text =  "Today "+targetFormat.format(date1)
                } catch (ex: ParseException) {
                }
            }else{
                viewHolder.binding.tvTime.text =formattedDate
            }
        } else{
            viewHolder.binding.tvTime.text = ""
        }

        viewHolder.binding.rrMain.setOnClickListener {
            itemClick.onItemClick(position,"")
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: RowMessagesBinding = DataBindingUtil.bind(view)!!
    }
}