package com.example.passgenius.domain.use_cases.dialogs

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.passgenius.R
import com.example.passgenius.domain.interfaces.AppLockTimeInterface
import com.example.passgenius.ui.adapters.AppLockTimeAdapter

class AppLockTimeDialog(
    private val context: Context,
    private val appLockInterface:AppLockTimeInterface,
    private var time:Int
) {

    var dialog =Dialog(context)
    private val adapter:AppLockTimeAdapter = AppLockTimeAdapter(context, appLockInterface, time )


    fun registerCallBack(appLockInterface: AppLockTimeInterface){






    }


    init {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.dialog_app_lock_time)
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog?.window?.setGravity(Gravity.CENTER)
        val recyclerView:RecyclerView = dialog.findViewById(R.id.recyclerView)
        val cancelButton:CardView = dialog.findViewById(R.id.cancel_button)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        cancelButton.setOnClickListener {
            dialog.hide()
        }
    }







}

