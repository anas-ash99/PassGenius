package com.example.passgenius.data.room.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.passgenius.domain.models.LoginItemModel
import com.example.passgenius.domain.models.SecureNoteModel

@Dao
interface NoteItemDao {

    @Upsert
    suspend fun addItem(item: SecureNoteModel)
    @Delete
    fun deleteItem(item: SecureNoteModel)
    @Query("DELETE FROM SecureNoteModel")
    suspend fun deleteAllItems()
    @Query("SELECT * FROM secureNoteModel ORDER BY title DESC")
    suspend fun getItemsOrderedByTitle(): MutableList<SecureNoteModel>
    @Query("SELECT * FROM secureNoteModel")
    suspend fun getAllItemsByDate(): MutableList<SecureNoteModel>


}