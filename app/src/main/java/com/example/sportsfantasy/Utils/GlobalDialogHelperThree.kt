package com.example.sportsfantasy.Utils

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.Gravity
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.sportsfantasy.Interface.BottomSheetItemClickListener
import com.example.sportsfantasy.R
import com.google.android.material.bottomsheet.BottomSheetDialog

class GlobalDialogHelperThree(context: Context, val listenerContext: BottomSheetItemClickListener) : BottomSheetDialog(context) {

    private lateinit var titleTextView: TextView
    private lateinit var cancelButton: TextView
    private lateinit var okButton: TextView
    private lateinit var edt_invite_code: EditText

    private var listener: BottomSheetItemClickListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.layout_btnsheet_join_invite_league, null)

        listener = listenerContext

        edt_invite_code = view.findViewById(R.id.edt_btm_sheet_three)
        cancelButton = view.findViewById(R.id.btn_sheet_three_cancel)
        okButton = view.findViewById(R.id.btn_sheet_three_ok)

        setContentView(view)
        setCanceledOnTouchOutside(true)
    }

    fun setDialogData(title: String? = context.getString(R.string.app_name)) {
        show()

        okButton.setOnClickListener {
            val code = edt_invite_code.text.trim().toString()
            if (code == "") {
                edt_invite_code.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake))
            } else{
                listener!!.onBtnSheetItemClicked(code)
            }
        }

        cancelButton.setOnClickListener {
            dismiss()
        }
    }
}