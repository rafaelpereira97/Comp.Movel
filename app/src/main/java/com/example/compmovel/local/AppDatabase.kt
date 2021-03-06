package com.example.compmovel.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Notes::class), version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao
}