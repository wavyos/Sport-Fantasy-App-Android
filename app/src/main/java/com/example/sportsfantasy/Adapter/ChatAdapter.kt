package com.example.sportsfantasy.Adapter
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsfantasy.Interface.ListItemClick
import com.example.sportsfantasy.Model.ChatResModel
import com.example.sportsfantasy.R
import com.example.sportsfantasy.databinding.RowChatBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


open class ChatAdapter(
    val context: Context,
    val userID: String,
    var list: ArrayList<ChatResModel.Message>,
    var itemClick: ListItemClick
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_chat, parent, false)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ViewHolder

        if(list[position].senderId.toString() == userID){

//            viewHolder.binding.txtSenderName.text = list[position].senderName
//            viewHolder.binding.txtSenderName.text = "You"
            viewHolder.binding.txtSenderMessage.text = list[position].message
            //viewHolder.binding.txtSenderTime.text = list[position].messageTime

            viewHolder.binding.llReceiveChat.visibility = View.GONE
            viewHolder.binding.llSenderChat.visibility = View.VISIBLE


            val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            df.timeZone = TimeZone.getTimeZone("UTC")
            val date = df.parse(list[position].messageTime!!)
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
                val date = df.parse(list[position].messageTime!!)
                df.timeZone = TimeZone.getDefault()
                val formattedDate = df.format(date)

                val originalFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val targetFormat = SimpleDateFormat("HH:mm")
                val date1: Date
                try {
                    date1 = originalFormat.parse(formattedDate)
                    viewHolder.binding.txtDateTime.visibility = View.GONE
                    viewHolder.binding.txtSenderTime.text =  "Today "+targetFormat.format(date1)
                } catch (ex: ParseException) {
                    // Handle Exception.
                }

            }else{

                val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
                df.timeZone = TimeZone.getTimeZone("UTC")
                val date = df.parse(list[position].messageTime!!)
                df.timeZone = TimeZone.getDefault()
                val formattedDate = df.format(date)

                val originalFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val targetFormat = SimpleDateFormat("yyyy-MM-dd")
                val targetFormat1 = SimpleDateFormat("HH:mm")
                val date1: Date
                val date2: Date
                try {
                    date1 = originalFormat.parse(formattedDate)
                    date2 = originalFormat.parse(formattedDate)
                    viewHolder.binding.txtDateTime.visibility = View.VISIBLE
                    viewHolder.binding.txtDateTime.text = targetFormat.format(date1)
                    viewHolder.binding.txtSenderTime.text = targetFormat1.format(date2)
                } catch (ex: ParseException) {
                    // Handle Exception.
                }

                //viewHolder.binding.txtSenderTime.text =formattedDate
            }


        }else{

            viewHolder.binding.txtReceiveName.text = list[position].senderName
            viewHolder.binding.txtReceiveMessage.text = list[position].message
            //viewHolder.binding.txtReceiveTime.text = list[position].messageTime

            val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            df.timeZone = TimeZone.getTimeZone("UTC")
            val date = df.parse(list[position].messageTime!!)
            df.timeZone = TimeZone.getDefault()
            val formattedDate = df.format(date)

            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            //sdf.timeZone = TimeZone.getTimeZone("UTC")
            df.timeZone = TimeZone.getDefault()
            val currentDate = sdf.format(Date())

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
                val date = df.parse(list[position].messageTime!!)
                df.timeZone = TimeZone.getDefault()
                val formattedDate = df.format(date)

                val originalFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val targetFormat = SimpleDateFormat("HH:mm")
                val date1: Date
                try {
                    date1 = originalFormat.parse(formattedDate)
                    viewHolder.binding.txtReceiveDateTime.visibility = View.GONE
                    viewHolder.binding.txtReceiveTime.text =  "Today "+targetFormat.format(date1)
                } catch (ex: ParseException) {
                    // Handle Exception.
                }
            }else{
                val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
                df.timeZone = TimeZone.getTimeZone("UTC")
                val date = df.parse(list[position].messageTime!!)
                df.timeZone = TimeZone.getDefault()
                val formattedDate = df.format(date)

                val originalFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val targetFormat = SimpleDateFormat("yyyy-MM-dd")
                val targetFormat1 = SimpleDateFormat("HH:mm")
                val date1: Date
                val date2: Date
                try {
                    date1 = originalFormat.parse(formattedDate)
                    date2 = originalFormat.parse(formattedDate)
                    viewHolder.binding.txtReceiveDateTime.visibility = View.VISIBLE
                    viewHolder.binding.txtReceiveDateTime.text = targetFormat.format(date1)
                    viewHolder.binding.txtReceiveTime.text = targetFormat1.format(date2)

                } catch (ex: ParseException) {
                    // Handle Exception.
                }

            }
            viewHolder.binding.llReceiveChat.visibility = View.VISIBLE
            viewHolder.binding.llSenderChat.visibility = View.GONE
        }

    }

    fun addMessage(chatList: List<ChatResModel.Message>) {
        list.clear()
        list.addAll(chatList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: RowChatBinding = DataBindingUtil.bind(view)!!
    }
}