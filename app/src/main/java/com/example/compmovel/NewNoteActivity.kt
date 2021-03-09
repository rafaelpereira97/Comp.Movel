package com.example.compmovel

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.whenResumed
import androidx.room.Room
import com.example.compmovel.local.AppDatabase
import com.example.compmovel.local.Notes
import java.time.LocalDateTime
import java.util.*

class NewNoteActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)
        setSupportActionBar(findViewById(R.id.toolbar))
        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)
        setTitle("Adicionar Nova Nota")

        val insertBtn = findViewById<Button>(R.id.addLocalNote)

        insertBtn.setOnClickListener {
            val noteTitle = findViewById<EditText>(R.id.noteTitle).text
            val noteDescription = findViewById<EditText>(R.id.noteDescription).text

            insertNote(noteTitle.toString(),noteDescription.toString())
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun insertNote(noteTitle: String, noteDescription: String){
        try {
            val db = openDbConnection()
            val date = LocalDateTime.now()
            val nota = Notes(null,noteTitle,noteDescription,"12312","32232",date.toString())
            db?.notesDao()?.insertAll(nota)
            db?.close()
            Toast.makeText(applicationContext,"Nota Inserida com Sucesso!",Toast.LENGTH_LONG).show()
            finish()
        }catch (exception: ExceptionInInitializerError){
            Toast.makeText(applicationContext,exception.toString(),Toast.LENGTH_LONG).show()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun openDbConnection(): AppDatabase? {

        return this.let {
            Room.databaseBuilder(
                it,
                AppDatabase::class.java, "notesdb"
            )
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
        }

    }
}