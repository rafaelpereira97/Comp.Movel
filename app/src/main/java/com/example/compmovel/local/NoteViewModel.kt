package com.example.compmovel.local

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NoteRepository
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allNotes: LiveData<List<Notes>>

    init {
        val notesDao = NoteDB.getDatabase(application, viewModelScope).notesDao()
        repository = NoteRepository(notesDao)
        allNotes = repository.getAll
    }



    fun insert(note: Notes) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(note)
    }

    fun deleteNote(note: Int) = viewModelScope.launch(Dispatchers.IO){
       repository.deleteNote(note)
    }

    fun update(note: Notes) = viewModelScope.launch(Dispatchers.IO){
        repository.update(note)
    }

}