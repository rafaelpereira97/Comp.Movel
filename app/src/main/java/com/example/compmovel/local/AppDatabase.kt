package com.example.compmovel.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Notes::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao
}