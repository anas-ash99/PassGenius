package com.example.passgenius.data.room.daos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.passgenius.domain.models.ItemModel
import com.example.passgenius.domain.models.LoginItemModel
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.Flow

@Dao
interface ItemDao {

    @Upsert
    suspend fun addItem(item: ItemModel)
    @Delete
    suspend fun deleteItem(item: ItemModel)
    @Query("DELETE FROM itemModel")
    suspend fun deleteAllItems()
    @Query("SELECT * FROM itemModel ORDER BY name DESC")
    fun getItemsOrderedByName():  LiveData<MutableList<ItemModel>>
    @Query("SELECT * FROM itemModel")
    suspend fun getAllItemsByDate():  MutableList<ItemModel>


}