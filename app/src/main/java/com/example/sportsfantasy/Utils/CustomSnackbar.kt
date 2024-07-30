package com.example.sportsfantasy.Utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import com.androidadvance.topsnackbar.TSnackbar
import com.example.sportsfantasy.R

class CustomSnackbar(val mactivity: Activity)
{
    private lateinit var snackbar: TSnackbar

    @SuppressLint("ResourceAsColor")
    fun ShowSnackBar(string:String)
    {

        snackbar = TSnackbar.make(mactivity.findViewById(android.R.id.content),string,TSnackbar.LENGTH_SHORT)
        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(R.color.bg)
        snackbar.setIconLeft(R.drawable.ic_warning, 18F)
        snackbar.setIconPadding(10)
        // snackbar.setIconPadding(5)

        val textView = snackbarView.findViewById<View>(com.androidadvance.topsnackbar.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        // textView.compoundDrawablePadding = 5
        textView.textSize = 12F
        snackbar.show()

    }


}