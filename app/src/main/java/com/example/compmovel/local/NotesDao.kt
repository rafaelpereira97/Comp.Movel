package com.example.compmovel.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NotesDao {
    @Query("SELECT * FROM notes")
    fun getAll(): List<Notes>

    @Insert
    fun insertAll(vararg notes: Notes)

    @Query("DELETE FROM notes WHERE note = :note")
    fun deleteNote(note: String)

}