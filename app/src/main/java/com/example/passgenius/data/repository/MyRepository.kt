package com.example.passgenius.data.repository

import android.content.SharedPreferences
import com.example.passgenius.data.dataStates.DataState
import com.example.passgenius.domain.models.*
import kotlinx.coroutines.flow.Flow

interface MyRepository {

   suspend fun getAllItems():Flow<DataState<MutableList<ItemListModel>>>

   suspend fun getItemsFromLocalDB(): Flow<DataState<MutableList<ItemListModel>>>

   suspend fun deleteNoteItem(item:SecureNoteModel)
   suspend fun deleteLoginItem(item:LoginItemModel)
   suspend fun saveLoginItemToRemoteDB(item: LoginItemModel):Flow<DataState<LoginItemModel>>
   suspend fun saveNoteItemToRemoteDB(item: SecureNoteModel):Flow<DataState<SecureNoteModel>>
   suspend fun getLoggedInUser(): UserModel?
   suspend fun updateLoggedInUser( user: UserModel)



}