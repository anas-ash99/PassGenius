package com.example.passgenius.common

import com.example.passgenius.domain.models.ItemListModel
import com.example.passgenius.domain.models.UserModel

object DataHolder {
    var loggedInUser : UserModel?  = null
    var allItems = mutableListOf<ItemListModel>()
    var favouriteItemsIds = mutableListOf<String>()

}