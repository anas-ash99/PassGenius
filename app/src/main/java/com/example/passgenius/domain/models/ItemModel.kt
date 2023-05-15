package com.example.passgenius.domain.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime


@Entity
data class ItemModel (
    @PrimaryKey(true)
    var id:Int=0,
    var name:String,
    var secondaryName:String,
    var type:String = "",
    var imageUrl:String? = "",
    @Embedded
    var noteItem: SecureNoteModel = SecureNoteModel(),
    @Embedded
    var loginItem: LoginItemModel = LoginItemModel(),
    var createdAt:String = LocalDateTime.now().toString()
        )