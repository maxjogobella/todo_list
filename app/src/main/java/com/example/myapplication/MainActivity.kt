package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.NoteAdapter
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var viewModel : MainViewModel
    private lateinit var noteAdapter : NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeComponents()
        launchOnNextActivity()
        attachItemTouchHelper()
        observe()
    }

    private fun initializeComponents() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        noteAdapter = NoteAdapter()
        binding.noteRecyclerView.adapter = noteAdapter
    }

    private fun attachItemTouchHelper() {
        val itemTouchHelper = ItemTouchHelper(object : SimpleCallback(0,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val note = noteAdapter.notes[position]
                viewModel.remove(note)
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.noteRecyclerView)
    }

    private fun observe() {
        viewModel.getErrorMessage().observe(this) {errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }

        viewModel.getSortedNotes().observe(this) {sortedList ->
            noteAdapter.notes = sortedList
        }
    }

    private fun launchOnNextActivity() {
        binding.buttonNextActivity.setOnClickListener {
            val intent = NoteActivity.newIntent(this)
            startActivity(intent)
        }
    }

}