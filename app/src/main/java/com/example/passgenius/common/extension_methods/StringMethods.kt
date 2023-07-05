package com.example.passgenius.common.extension_methods

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast

object StringMethods {



    fun String.shortName(): String{
        if(this.isEmpty()){
            return ""
        }


        val words = this.split(" ")
        println(words.size)
        if (words.size == 1){
            return "${words[0].substring(0,1).uppercase()}${words[0].substring(1,2).uppercase()}"
        }

        if (words.size == 2){

            return words[0].substring(0,1).uppercase() + words[1].substring(0,1).uppercase()
        }

        if (words.size == 3){
            return words[0].substring(0,1).uppercase() + words[2].substring(0,1).uppercase()
        }
        return ""

    }


    fun String.copyText(context: Context){
        val clipBoard: ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText("value",this)
        clipBoard.setPrimaryClip(clip)
        Toast.makeText(context, "Copied to clipboard.", Toast.LENGTH_SHORT).show()
    }


}