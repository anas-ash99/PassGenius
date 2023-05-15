package com.example.passgenius.data.di

import android.app.Application
import androidx.room.Room
import com.example.passgenius.data.room.ItemDatabase
import com.example.passgenius.data.room.daos.LoginItemDao
import com.example.passgenius.data.room.daos.NoteItemDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RoomModule {


    @Provides
    @Singleton
    fun getDB(context: Application): ItemDatabase {
        return Room.databaseBuilder(
            context,
            ItemDatabase::class.java,
            "itemsDB")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideLoginItemDao(db: ItemDatabase): LoginItemDao {
        return db.loginItemsDao
    }


    @Provides
    @Singleton
    fun provideNoteItemDao(db: ItemDatabase): NoteItemDao {
        return db.noteItemsDao
    }




}