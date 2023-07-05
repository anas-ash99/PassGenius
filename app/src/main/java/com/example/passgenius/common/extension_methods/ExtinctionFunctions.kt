package com.example.passgenius.common.extension_methods

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import com.example.passgenius.domain.models.ItemListModel
import com.example.passgenius.domain.models.LoginItemModel

object ExtinctionFunctions {




    fun String.copyText(context:Context){
        val clipBoard: ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText("value",this)
        clipBoard.setPrimaryClip(clip)
        Toast.makeText(context, "Copied to clipboard.", Toast.LENGTH_SHORT).show()
    }


}