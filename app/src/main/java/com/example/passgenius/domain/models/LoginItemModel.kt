package com.example.passgenius.domain.models


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID



@Entity()
data class LoginItemModel(

    @PrimaryKey(false)
    var _id:String = UUID.randomUUID().toString(),
    var itemName:String = "",
    var email:String ="",
    var username:String = "",
    var password:String = "",
    var linkAddress: String = "",
    var note:String = "",
    var website:String ="",
    var createdSAt: String = ""
) : java.io.Serializable
