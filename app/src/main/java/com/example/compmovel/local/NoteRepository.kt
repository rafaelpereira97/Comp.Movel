package com.example.compmovel.local

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class NoteRepository(private val noteDao: NotesDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    //val allWords: Flow<List<Notes>> = noteDao.getAll()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(note: Notes) {
        noteDao.insertAll(note)
    }

    val getAll: LiveData<List<Notes>> = noteDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(note: Notes) {
        noteDao.update(note)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteNote(note: Int) {
        noteDao.deleteNote(note)
    }

}
