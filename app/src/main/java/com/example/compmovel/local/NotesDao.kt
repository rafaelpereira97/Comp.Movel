package com.example.compmovel.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NotesDao {
    @Query("SELECT * from notes order by uid desc")
    fun getAll(): LiveData<List<Notes>>

    @Insert
    fun insertAll(vararg notes: Notes)

    @Update
    fun update(vararg note: Notes)

    @Query("DELETE FROM notes WHERE uid = :uid")
    fun deleteNote(uid: Int)

}