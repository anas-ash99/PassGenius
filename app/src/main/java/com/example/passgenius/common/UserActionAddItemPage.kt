package com.example.passgenius.common

sealed class UserActionAddItemPage{
    object EditItem:UserActionAddItemPage()
    object SaveToFavourite:UserActionAddItemPage()
    object DeleteItem:UserActionAddItemPage()
    object OnBackPressed:UserActionAddItemPage()
}
