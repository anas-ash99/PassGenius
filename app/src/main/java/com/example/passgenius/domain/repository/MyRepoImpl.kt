package com.example.passgenius.domain.repository

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.util.Log
import com.example.passgenius.data.apis.ItemsApi
import com.example.passgenius.data.dataStates.DataState
import com.example.passgenius.domain.models.*
import com.example.passgenius.data.repository.MyRepository
import com.example.passgenius.data.room.daos.LoginItemDao
import com.example.passgenius.data.room.daos.NoteItemDao
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.awaitResponse
import java.io.IOException
import javax.inject.Inject

class MyRepoImpl @Inject constructor(
    private val itemsApi: ItemsApi,
    private val loginItemDao: LoginItemDao,
    private val noteItemDao: NoteItemDao,
    private val mPrefs: SharedPreferences

): MyRepository {

    override suspend fun getItemsFromLocalDB(): Flow<DataState<MutableList<ItemListModel>>> = channelFlow{
        val items:MutableList<ItemListModel> = mutableListOf()
        send(DataState.Loading)
        try {

            loginItemDao.getItemsOrderedByName().onEach {
                items.add(ItemListModel(name = it.itemName, secondaryName = it.email, loginItem = it, type = "LOGIN"))
            }

            noteItemDao.getItemsOrderedByTitle().onEach {
                items.add(ItemListModel(name = it.title, secondaryName = it.content, noteItem = it, type = "NOTE"))
            }

            send(DataState.Success(items))
        }catch (e:Exception){
            send(DataState.Error(e))
        }


    }

    override suspend fun getLoginItemsRV(): MutableList<ItemListModel> {
        val items:MutableList<ItemListModel> = mutableListOf()
        loginItemDao.getItemsOrderedByName().onEach {
            items.add(ItemListModel(name = it.itemName, secondaryName = it.email, loginItem = it, type = "LOGIN"))
        }
        return items
    }

    override suspend fun saveLoginItemToRemoteDB(item: LoginItemModel): Flow<DataState<LoginItemModel>>  = flow{
        emit(DataState.Loading)
        try {
            val res = itemsApi.saveLoginItem(item).awaitResponse().body()!!
            loginItemDao.addItem(res)
            emit(DataState.Success(res))
        }catch (e:Exception){
            emit(DataState.Error(e))
            Log.e("repo", e.toString())
        }

    }

    override suspend fun saveNoteItemToRemoteDB(item: SecureNoteModel): Flow<DataState<SecureNoteModel>> = flow {
        emit(DataState.Loading)
        try {
            val res = itemsApi.saveNoteItem(item).awaitResponse()
            if (res.isSuccessful){
                noteItemDao.addItem(res.body()!!)
                emit(DataState.Success(res.body()!!))

            }else{
                emit(DataState.Error(java.lang.Exception()))
            }
        }catch (e:Exception){
            emit(DataState.Error(e))
            Log.e("repo", e.toString())
        }
    }

     override suspend fun getNoteItemsRV(): MutableList<ItemListModel> {
        val items:MutableList<ItemListModel> = mutableListOf()
        noteItemDao.getItemsOrderedByTitle().onEach {
            items.add(ItemListModel(name = it.title, secondaryName = it.content, noteItem = it, type = "NOTE"))
        }
        return items
    }

    override suspend fun deleteNoteItem(item:SecureNoteModel) {
        try {
            noteItemDao.deleteItem(item)
        }catch (e:Exception) {
            Log.e("repo", e.toString())
        }

    }

    override suspend fun deleteLoginItem(item:LoginItemModel) {
        try {

            loginItemDao.deleteItem(item)

        }catch (e:Exception) {
            Log.e("delete item", e.toString())
        }
    }


    override suspend fun getAllItems():Flow<DataState<MutableList<ItemListModel>>> = flow {
        emit(DataState.Loading)

        try {
            val items:MutableList<ItemListModel> = mutableListOf()
            val res = itemsApi.getLoginItemsRemote().awaitResponse()
             if (res.isSuccessful){
                loginItemDao.deleteAllItems()
                noteItemDao.deleteAllItems()
                res.body()!!.onEach {
                    loginItemDao.addItem(it)
                }

                itemsApi.getNoteItemsItemsRemote().awaitResponse().body()!!.onEach {
                    noteItemDao.addItem(it)

                }

                loginItemDao.getItemsOrderedByName().onEach {

                    items.add(ItemListModel(name = it.itemName, secondaryName = it.email, loginItem = it, type = "LOGIN"))
                }
                 noteItemDao.getItemsOrderedByTitle().onEach {
                    items.add(ItemListModel(name = it.title, secondaryName = it.content, noteItem = it, type = "NOTE"))
                }

                emit(DataState.Success(items))
            }

        }catch (e:HttpException){
            Log.e("repo", e.toString())
            emit(DataState.Error(e))

        }catch (e: IOException){
            /// no internet connection error
            Log.e("repo", e.toString())
            emit(DataState.Error(e))


        }
    }

    override suspend fun getLoggedInUser(): UserModel? {
        val gson = Gson()
        val json = mPrefs.getString("LoggedInUser", "")
        return gson.fromJson(json, UserModel::class.java)
    }

    override suspend fun updateLoggedInUser(user: UserModel) {
        val prefsEditor: Editor = mPrefs.edit()
        val gson = Gson()
        val json = gson.toJson(user)
        prefsEditor.putString("LoggedInUser", json)
        prefsEditor.apply()
    }





    override suspend fun getNoteItemsRemote(): Flow<MutableList<SecureNoteModel>> = flow {

    }

    override suspend fun getLoginItemsRemote(): Flow<MutableList<LoginItemModel>> = flow {
        try {

        }catch (e:Exception){
            Log.e("repo", e.toString())
        }

    }



}


