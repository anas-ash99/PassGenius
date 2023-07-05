package com.example.passgenius.data.room.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.passgenius.domain.models.LoginItemModel


@Dao
interface LoginItemDao {


    @Upsert
    suspend fun addItem(item: LoginItemModel)

    @Delete
    suspend fun deleteItem(item: LoginItemModel)

    @Query("DELETE FROM loginItemModel")
    suspend fun deleteAllItems()

    @Query("SELECT * FROM loginItemModel ORDER BY itemName DESC")
    suspend fun getItemsOrderedByName(): MutableList<LoginItemModel>

    @Query("SELECT * FROM loginItemModel")
    suspend fun getAllItemsByDate():  MutableList<LoginItemModel>
}