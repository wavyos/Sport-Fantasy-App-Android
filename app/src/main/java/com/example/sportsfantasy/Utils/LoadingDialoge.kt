package com.example.sportsfantasy.Utils

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.sportsfantasy.R
import de.hdodenhof.circleimageview.CircleImageView

class LoadingDialoge(val mactivity:Activity)
{
    private lateinit var isdialoge:AlertDialog

    fun StartLoading(){
        //set view
        val inflater = mactivity.layoutInflater
        val dialogeView = inflater.inflate(R.layout.custom_dialogebox,null)
        val builder = AlertDialog.Builder(mactivity)

        builder.setView(dialogeView)
        builder.setCancelable(false)
        isdialoge = builder.create()
        isdialoge.show()


    }

    fun isDismiss()
    {
        isdialoge.dismiss()
    }
}