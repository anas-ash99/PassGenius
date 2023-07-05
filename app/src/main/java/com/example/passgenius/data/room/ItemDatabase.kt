package com.example.passgenius.data.room

import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import com.example.passgenius.domain.models.LoginItemModel
import com.example.passgenius.domain.models.SecureNoteModel
import com.example.passgenius.data.room.daos.LoginItemDao

import com.example.passgenius.data.room.daos.NoteItemDao


@Database(
    entities = [LoginItemModel::class, SecureNoteModel::class],
    version = 1,
//    autoMigrations = [
//        AutoMigration(from = 3, to =4),
//    ]

)

abstract class ItemDatabase: RoomDatabase() {

    abstract val loginItemsDao: LoginItemDao
    abstract val noteItemsDao: NoteItemDao

//
//
//    @DeleteColumn("LoginItemModel", "created_at")
//    interface Migration2to3:AutoMigrationSpec
}