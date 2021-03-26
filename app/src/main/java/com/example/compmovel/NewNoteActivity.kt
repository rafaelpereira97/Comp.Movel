package com.example.compmovel

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.compmovel.local.NoteRepository
import com.example.compmovel.local.NoteViewModel
import com.example.compmovel.local.Notes
import java.time.LocalDateTime

class NewNoteActivity : AppCompatActivity() {

    private lateinit var noteViewModel: NoteViewModel

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
        if(noteTitle != "") {
            try {
                val date = LocalDateTime.now()
                val nota = Notes(null, noteTitle, noteDescription, "12312", "32232", date.toString())
                noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
                noteViewModel.insert(nota)
                Toast.makeText(applicationContext, "Nota Inserida com Sucesso!", Toast.LENGTH_LONG).show()
                finish()
            } catch (exception: ExceptionInInitializerError) {
                Toast.makeText(applicationContext, exception.toString(), Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(this, "A nota necessita de um título !", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}