package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.myapplication.databinding.NoteItemBinding
import com.example.myapplication.model.Note

class NoteAdapter : Adapter<NoteAdapter.NoteViewHolder>()  {

    var notes = listOf<Note>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var onNoteClickListener : OnNoteClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.noteText.text = note.text
        holder.titleText.text = note.title

        val colorResId: Int = when (note.priority) {
            1 -> android.R.color.holo_green_light
            2 -> android.R.color.holo_orange_light
            else -> android.R.color.holo_red_light
        }
        val color = ContextCompat.getColor(holder.itemView.context, colorResId)
        holder.noteText.setBackgroundColor(color)
        holder.titleText.setBackgroundColor(color)
    }

    override fun getItemCount() = notes.size

    inner class NoteViewHolder(binding: NoteItemBinding) : ViewHolder(binding.root) {
        val noteText = binding.noteText
        val titleText = binding.titleText
    }

    interface OnNoteClickListener {
        fun onNoteClick(note : Note)
    }

}