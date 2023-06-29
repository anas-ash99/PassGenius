package com.example.passgenius.common

import com.example.passgenius.ui.adapters.AdapterCategories

sealed class UserActions{
    data class NavigateScreens(val screen:String) :UserActions()
    object PlusOrXClick:UserActions()
    data class ChoseItemsCategory(val position:Int,val adapter:AdapterCategories):UserActions()
}
