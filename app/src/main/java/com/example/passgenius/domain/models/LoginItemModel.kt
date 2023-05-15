package com.example.passgenius.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.Date
import java.util.UUID


@Entity
data class LoginItemModel(

    @PrimaryKey(false)
    @ColumnInfo("_id", defaultValue = "")
    var _id:String =  "",
    val id:Int = 0,
    var itemName:String = "",
    var email:String ="",
    var username:String = "",
    var password:String = "",
    var linkAddress: String = "",
    var note:String = "",
    var website:String ="",
    var createdSAt: String = ""
) : java.io.Serializable
