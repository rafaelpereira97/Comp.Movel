package com.example.compmovel

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.compmovel.local.Notes

class LocalNotesAdapter(private val dataSet: ArrayList<Notes>?) :
    RecyclerView.Adapter<LocalNotesAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.noteTitle)
        val noteDescription: TextView = view.findViewById(R.id.noteDescription)

        init {
            view.setOnClickListener {
                println(view.findViewById<TextView>(R.id.noteTitle).text)
                val intent = Intent(view.context, EditNoteActivity::class.java)
                intent.putExtra("NOTE", "nota")
                view.context.startActivity(intent)
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.text_row_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textView.text = dataSet!![position].note
        viewHolder.noteDescription.text = dataSet[position].description
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet!!.size

    fun removeAt(position: Int) {
        dataSet!!.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getNoteId(position: Int): Int? {
        return dataSet!![position].uid
    }

    fun getNote(position: Int): Notes? {
        return dataSet!![position]
    }
}
