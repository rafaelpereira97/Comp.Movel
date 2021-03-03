package com.example.compmovel.ui.notifications

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.compmovel.LocalNotesAdapter
import com.example.compmovel.R
import com.example.compmovel.local.AppDatabase
import com.example.compmovel.local.Notes

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
                ViewModelProvider(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        setHasOptionsMenu(true)

        val db = context?.let {
            Room.databaseBuilder(
                it,
                AppDatabase::class.java, "notesdb"
            )
                .allowMainThreadQueries()
                .build()
        }

        //val nota = Notes(7,"Teste nota","teste descricao","12312","32232","20/02/2021")
        //db?.notesDao()?.insertAll(nota)


        val notes = db?.notesDao()?.getAll()

        val localNotesRecyclerView = root.findViewById<RecyclerView>(R.id.localNotesRecyclerView)

        localNotesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = LocalNotesAdapter(notes as ArrayList<Notes>?)
            itemAnimator = DefaultItemAnimator()
        }




        db?.close()

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.notesmenu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_localnote -> {
                // navigate to add local note screen
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}