package com.example.passgenius.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.*


@Entity()
data class SecureNoteModel(


    @PrimaryKey(false)
    var _id:String = UUID.randomUUID().toString(),
    var title:String ="",
    var content:String="",
    var createdSAt: String = "${LocalDateTime.now()}"
): java.io.Serializable
