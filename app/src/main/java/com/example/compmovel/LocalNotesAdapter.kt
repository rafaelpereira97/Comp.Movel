package com.example.compmovel

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.compmovel.local.Notes

/*class LocalNotesAdapter(private val dataSet: ArrayList<Notes>?) : RecyclerView.Adapter<LocalNotesAdapter.ViewHolder>() {*/
class LocalNotesAdapter(dataSet: ArrayList<Notes>, listener: LocalNotesAdapterListener, context: Context) : RecyclerView.Adapter<LocalNotesAdapter.ViewHolder>() {
    private var dataSet: ArrayList<Notes>
    private val listener: LocalNotesAdapterListener

    init {
        this.listener = listener!!
        this.dataSet = dataSet
    }
/**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        var textView: TextView
        var noteDescription: TextView

        init {
             textView = view.findViewById(R.id.noteTitle)
             noteDescription = view.findViewById(R.id.noteDescription)
            view.setOnClickListener {
                listener.onNoteSelected(dataSet[adapterPosition])
            }
        }
    }

    interface LocalNotesAdapterListener {
        fun onNoteSelected(note: Notes?)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocalNotesAdapter.ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.text_row_item, parent, false)
        return ViewHolder(itemView)
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

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textView.text = dataSet!![position].note
        viewHolder.noteDescription.text = dataSet[position].description
    }
}
