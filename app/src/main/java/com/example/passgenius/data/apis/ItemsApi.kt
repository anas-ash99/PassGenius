package com.example.passgenius.data.apis

import com.example.passgenius.data.dataStates.DataState
import com.example.passgenius.domain.models.*
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ItemsApi {


    @GET("/note_items/get_all_items")
    fun getListItemsRemote(): Call<MutableList<ItemModel>>
    @GET("/Login_items/get_all_items")
    fun getLoginItemsRemote(): Call<MutableList<LoginItemModel>>
    @GET("/note_items/get_note_items")
    fun getNoteItemsItemsRemote(): Call<MutableList<SecureNoteModel>>

    @POST("/login_items/create_new_item")
    fun saveLoginItem(@Body item: LoginItemModel): Call<LoginItemModel>

    @POST("/note_items/create_new_item")
    fun saveNoteItem(@Body item: SecureNoteModel): Call<SecureNoteModel>

}