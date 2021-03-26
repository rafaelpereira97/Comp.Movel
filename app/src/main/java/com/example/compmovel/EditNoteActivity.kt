package com.example.compmovel

import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.compmovel.local.NoteViewModel
import com.example.compmovel.local.Notes

class EditNoteActivity : AppCompatActivity() {

    lateinit var title: String
    lateinit var description: String
    var id: Int = 0
    var titleEdit: EditText? = null
    var descriptionEdit: EditText? = null
    private lateinit var noteViewModel: NoteViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)
        setSupportActionBar(findViewById(R.id.toolbar))

        setTitle("Editar Nota - ")

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        id = intent.getIntExtra("id",0)
        title = intent.getStringExtra("title").toString()
        description = intent.getStringExtra("description").toString()


        titleEdit = findViewById<EditText>(R.id.noteTitle)
        descriptionEdit = findViewById<EditText>(R.id.noteDescription)

        titleEdit!!.setText(title)
        descriptionEdit!!.setText(description)

        val editLocalNoteButton = findViewById<Button>(R.id.editLocalNote)

        editLocalNoteButton.setOnClickListener {
            update()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun update(){
        if(titleEdit!!.text.toString() == ""){
            Toast.makeText(this, "A Nota necessita de um título!", Toast.LENGTH_SHORT).show()
        }else {
            noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
            val nota = Notes(uid = id, note = titleEdit!!.text.toString(), description = descriptionEdit!!.text.toString(), "12312", "32232", null)
            noteViewModel.update(nota)
            finish()
        }
    }
}