package com.example.sportsfantasy.Utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import com.example.sportsfantasy.Interface.BottomSheetItemClickListener
import com.example.sportsfantasy.R
import com.google.android.material.bottomsheet.BottomSheetDialog


class GlobalDialogHelper(context: Context, val listenerContext: BottomSheetItemClickListener) : BottomSheetDialog(context) {

    private lateinit var titleTextView: TextView
    private lateinit var subTitleTextView: TextView
    private lateinit var cancelButton: TextView
    private lateinit var okButton: TextView
    private lateinit var img_logo: ImageView

    private var listener: BottomSheetItemClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.layout_bottomsheet_popup_dialog, null)
        listener = listenerContext
        cancelButton = view.findViewById(R.id.btn_sheet_cancel)
        okButton = view.findViewById(R.id.btn_sheet_ok)
        titleTextView = view.findViewById(R.id.tv_popup_title)
        img_logo = view.findViewById(R.id.img_btmsheet_logo)
        subTitleTextView = view.findViewById(R.id.tv_popup_subtitle)

        setContentView(view)
        setCanceledOnTouchOutside(true)

        okButton.setOnClickListener {
            listener!!.onBtnSheetItemClicked("ok")
        }
        cancelButton.setOnClickListener {
            dismiss()
        }
    }

    fun setDialogData(
        title: String? = context.getString(R.string.app_name),
        subtitle: String? = "SubTitle",
        isTwo: Boolean? = true,
    ) {
        titleTextView.text = title
        subTitleTextView.text = subtitle
        show()
    }
}