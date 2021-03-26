package com.example.compmovel.ui.notifications

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.compmovel.*
import com.example.compmovel.local.NoteViewModel
import com.example.compmovel.local.Notes

class NotificationsFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener,LocalNotesAdapter.LocalNotesAdapterListener {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private lateinit var localNotesRecyclerView: RecyclerView
    private lateinit var noteViewModel: NoteViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
                ViewModelProvider(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        setHasOptionsMenu(true)

        localNotesRecyclerView = root.findViewById(R.id.localNotesRecyclerView)


        fillList()

        return root
    }


    fun fillList(){
        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        val notes = noteViewModel.allNotes

        noteViewModel.allNotes.observe(viewLifecycleOwner, Observer { notes ->
            val notesList = notes as MutableList<Notes>
            localNotesRecyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = context?.let { LocalNotesAdapter(notesList as ArrayList<Notes>, this@NotificationsFragment, it) }
                itemAnimator = DefaultItemAnimator()
            }
        })



        //puxar para o lado para apagar o item da lista e da BD
        swipeToDelete(localNotesRecyclerView)
    }


    private fun swipeToDelete(recyclerView: RecyclerView){
        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage("Tem a certeza que pertende apagar a Nota ?")
                        .setCancelable(false)
                        .setPositiveButton("Sim") { dialog, id ->
                            val adapter = recyclerView.adapter as LocalNotesAdapter

                            try {

                                adapter.getNoteId(viewHolder.adapterPosition)?.let {
                                    noteViewModel.deleteNote(it)
                                }
                                adapter.removeAt(viewHolder.adapterPosition)

                            }catch (e: Exception){
                                Toast.makeText(requireContext(),e.toString(),Toast.LENGTH_LONG).show()
                            }
                        }
                        .setNegativeButton("Não") { dialog, id ->
                            dialog.dismiss()
                        }
                val alert = builder.create()
                alert.show()

            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }


    override fun onResume() {
        super.onResume()
        fillList()
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


    override fun onRefresh() {
        fillList()
    }

    override fun onNoteSelected(note: Notes?) {
        val intent = Intent(context, EditNoteActivity::class.java).apply {
            if (note != null) {
                putExtra("id",note.uid)
                putExtra("title",note.note)
                putExtra("description",note.description)
            }
        }

        startActivity(intent)
    }
}