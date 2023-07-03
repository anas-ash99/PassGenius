package com.example.passgenius.common

import com.example.passgenius.common.enums.AddItemType
import com.example.passgenius.domain.models.ItemListModel
import com.example.passgenius.ui.adapters.AdapterCategories

sealed class UserActions{
    data class NavigateScreens(val screen:String) :UserActions()
    object PlusOrXClick:UserActions()
    data class ChoseItemsCategory(val position:Int,val adapter:AdapterCategories):UserActions()
    data class ItemMenuClick(val position: Int,val item:ItemListModel): UserActions()
    data class CopyDetail(val data:String):UserActions()
    object DeleteItem:UserActions()
    object DismissDialog:UserActions()
}
