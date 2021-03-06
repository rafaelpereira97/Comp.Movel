package com.example.compmovel.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.compmovel.*
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

        val db = openDbConnection()

        val notes = db?.notesDao()?.getAll()

        db?.close()

        val localNotesRecyclerView = root.findViewById<RecyclerView>(R.id.localNotesRecyclerView)

        localNotesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = LocalNotesAdapter(notes as ArrayList<Notes>?)
            itemAnimator = DefaultItemAnimator()
        }


        //puxar para o lado para apagar o item da lista e da BD
        swipeToDelete(localNotesRecyclerView)



        return root
    }


    private fun swipeToDelete(recyclerView: RecyclerView){
        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = recyclerView.adapter as LocalNotesAdapter

                val db = openDbConnection()

                db?.notesDao()?.deleteNote(viewHolder.itemView.findViewById<TextView>(R.id.noteTitle).text as String)

                db?.close()

                adapter.removeAt(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.notesmenu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_localnote -> {
                val intent = Intent(requireContext(), NewNoteActivity::class.java)
                requireContext().startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun openDbConnection(): AppDatabase? {

        return context?.let {
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