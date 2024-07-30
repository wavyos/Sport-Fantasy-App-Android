package com.example.sportsfantasy.Utils

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.NestedScrollView
import com.example.sportsfantasy.Interface.BottomSheetItemClickListener
import com.example.sportsfantasy.R
import com.google.android.material.bottomsheet.BottomSheetDialog

class GlobalDialogHelperTwo(context: Context, val listenerContext: BottomSheetItemClickListener) : BottomSheetDialog(context) {

    private lateinit var titleTextView: TextView
    private lateinit var cancelButton: TextView
    private lateinit var ll_button_container: LinearLayout
    private lateinit var scrollview: NestedScrollView

    private var listener: BottomSheetItemClickListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.layout_btmsheet_popup_two, null)

        listener = listenerContext

        titleTextView = view.findViewById(R.id.tv_popup_two_title)
        cancelButton = view.findViewById(R.id.tv_popup_two_cancel)
        ll_button_container = view.findViewById(R.id.buttonContainer)
        scrollview = view.findViewById(R.id.nested_scroll_dialog_two)

        setContentView(view)
        setCanceledOnTouchOutside(true)

        cancelButton.setOnClickListener {
            dismiss()
        }
    }

    fun setDialogData(
        title: String? = context.getString(R.string.app_name),
        buttonLabels: ArrayList<String>? = null,
        hasIndex : String? = "",
    ) {
        titleTextView.text = title


        val ll_params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 120)
        ll_params.gravity = Gravity.CENTER
        ll_params.bottomMargin = 15
        val face = ResourcesCompat.getFont(context, R.font.roboto_bold)

        if(buttonLabels!!.size <= 4){
            val layoutParams = scrollview.layoutParams
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            scrollview.layoutParams = layoutParams
        }

        for (label in buttonLabels!!) {
            val button = TextView(context)
            button.text = label
            button.typeface = face
            button.gravity = Gravity.CENTER
//                button.textAlignment = View.TEXT_ALIGNMENT_CENTER
            if(buttonLabels.size <= 4){
                button.background = context.getDrawable(R.drawable.orange_border_view)
                button.setTextColor(context.getColor(R.color.bg))
            }else{
                button.background = context.getDrawable(R.drawable.rounded_edittext)
                button.setTextColor(context.getColor(R.color.black))
            }

            button.layoutParams = ll_params

            if(hasIndex != ""){
                if(hasIndex!!.toInt() == buttonLabels.indexOf(label)){
                    button.backgroundTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.bg))
                    button.setTextColor(context.getColor(R.color.white))
                }
            }
            // Set click listener for each button
            button.setOnClickListener {
                // Handle button click here
                button.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(context, R.color.bg))
                button.setTextColor(context.getColor(R.color.white))
                listener!!.onBtnSheetItemClicked(label)
                dismiss()
            }

            // Add the button to the button container
            ll_button_container.addView(button)
        }

        show()
    }
}