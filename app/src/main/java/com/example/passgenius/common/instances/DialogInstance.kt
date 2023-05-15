package com.example.passgenius.common.instances

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import com.example.passgenius.R


object DialogInstance  {
    private var dialog:Dialog? = null

    fun initDialog(context: Context, gravity:Int, layout:Int, animation:Int): Dialog?{

        dialog = Dialog(context)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(layout)
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.window?.attributes?.windowAnimations = animation
        dialog?.window?.setGravity(gravity)
        return dialog

    }




}
