package com.example.passgenius.common

import androidx.lifecycle.MutableLiveData
import com.example.passgenius.domain.models.ItemListModel
import com.example.passgenius.domain.models.UserModel

object DataHolder {
    var loggedInUser : UserModel?  =null
    val allItems: MutableLiveData<MutableList<ItemListModel>> = MutableLiveData()
    init {
        allItems.value = mutableListOf()
    }
}