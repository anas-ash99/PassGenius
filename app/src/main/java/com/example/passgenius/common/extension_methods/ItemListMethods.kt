package com.example.passgenius.common.extension_methods

import com.example.passgenius.common.enums.AddItemType
import com.example.passgenius.domain.models.ItemListModel
import com.example.passgenius.domain.models.LoginItemModel
import com.example.passgenius.domain.models.SecureNoteModel

object ItemListMethods {

    fun MutableList<ItemListModel>.updateOrAddLogInItem(item: LoginItemModel){
        var isFound = false
        this.forEach {
            if (it.loginItem._id == item._id){
                it.name = item.itemName
                it.secondaryName = item.email
                it.loginItem = item
                isFound = true
            }
        }

        if (!isFound){
            this.add(ItemListModel(item.itemName,item.email, loginItem = item, type = "LOGIN"))
        }

    }



    fun MutableList<ItemListModel>.updateOrAddNoteItem(item:SecureNoteModel){
        var isFound = false
        this.forEach {
            if (it.noteItem._id == item._id){
                it.name = item.title
                it.secondaryName = item.content
                it.noteItem = item
                isFound = true
            }
        }

        if (!isFound){
            this.add( ItemListModel(item.title,item.content, noteItem =  item, type = "NOTE"))
        }

    }
}