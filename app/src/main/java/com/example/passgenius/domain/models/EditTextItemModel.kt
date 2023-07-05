package com.example.passgenius.domain.models

import android.text.InputType
import com.example.passgenius.common.enums.EditTextType

data class EditTextItemModel(
    var name:String,
    var type:Int,
    var value: String,
    var isValid:Boolean = true
    )