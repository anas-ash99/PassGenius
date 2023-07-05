package com.example.passgenius.domain.models

import java.time.LocalDateTime



data class ItemListModel (

    var name:String,
    var secondaryName:String,
    var type:String = "",
    var typeString: String = "",
    var imageUrl:String = "",
    var noteItem: SecureNoteModel = SecureNoteModel(),
    var loginItem: LoginItemModel = LoginItemModel(),
    var createdAt:String = LocalDateTime.now().toString()
):java.io.Serializable